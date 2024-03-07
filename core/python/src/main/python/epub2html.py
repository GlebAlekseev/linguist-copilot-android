import io
import os
import shutil
import subprocess
import tempfile
import zipfile
from bs4 import BeautifulSoup
from shutil import copyfile
from xml.dom.minidom import parseString, Document


def read_xml(filename):
    """Read an XML file and return the DOM tree."""
    with open(filename, 'r') as f:
        data = f.read()
    return parseString(data)

def get_opf_file_path(epub_dir):
    """Read the container XML file at epub_dir and return the path of the *.opf file."""
    dom = read_xml(os.path.join(epub_dir, 'META-INF/container.xml'))
    xmlTag = dom.getElementsByTagName('rootfile')[0]
    return xmlTag.attributes['full-path'].value

def get_toc_file_path(opf_file):
    """Return the filename of the file containing the table of contents."""
    dom = read_xml(opf_file)
    xmlTag = dom.getElementsByTagName('manifest')[0].getElementsByTagName('item')
    toc_file = None
    for node in xmlTag:
        if node.attributes['id'].value == "ncx":
            toc_file = node.attributes['href'].value
            break
    if toc_file is None:
        raise ValueError("Table of contents file not found in OPF manifest.")
    root_dir = os.path.dirname(opf_file)
    return os.path.join(root_dir, toc_file)

def get_css_file_path_list(opf_file):
    """Return a list of file paths for CSS files listed in the OPF manifest."""
    dom = read_xml(opf_file)
    xmlTag = dom.getElementsByTagName('manifest')[0].getElementsByTagName('item')
    css_files = []
    for node in xmlTag:
        media_type = node.getAttribute('media-type')
        if media_type == "text/css":
            css_file = node.getAttribute('href')
            css_files.append(css_file)
    if not css_files:
        raise ValueError("No CSS files found in OPF manifest.")
    return css_files

def extract_content(root_dir, opf_file):
    dom = read_xml(opf_file)
    xmlTag = dom.getElementsByTagName('package')[0].getElementsByTagName('spine')[
        0].getElementsByTagName('itemref')
    spinelist = [node.attributes['idref'].value for node in xmlTag]

    xmlTag = dom.getElementsByTagName('package')[0].getElementsByTagName('manifest')[
        0].getElementsByTagName('item')
    manifest = {node.attributes['id'].value: node.attributes['href'].value for node in xmlTag}

    content = []
    for ref in spinelist:
        chap = [os.path.join(root_dir, manifest[ref])]
        xmlTag = read_xml(chap[0])
        for node in xmlTag.getElementsByTagName('body')[0].childNodes:
            chap.append(node)
        content.append(chap)

    return content

def get_chapter_list(epub_dir, root_dir, toc_file):
    """Build a list containing chapter names, their filenames, etc."""
    table_of_contents = []
    dom = read_xml(toc_file)
    xmlTag = dom.getElementsByTagName('navMap')[0].getElementsByTagName('navPoint')
    for node in xmlTag:
        chapter_title = \
        node.getElementsByTagName('navLabel')[0].getElementsByTagName('text')[0].childNodes[
            0].nodeValue
        chapter_link = node.getElementsByTagName('content')[0].attributes['src'].value
        if '#' in chapter_link:
            chapter_file, chapter_anchor = os.path.join(root_dir, chapter_link).split('#')
        else:
            chapter_file = os.path.join(root_dir, chapter_link)
            chapter_anchor = ''
        table_of_contents.append([chapter_title, chapter_file, chapter_anchor])
    return table_of_contents

def create_head_section(doc, epub_dir=None, css=None, jquery=None, dropdown=None,
                        table_of_contents=None,
                        book_file=None, css_files=None):
    """Build the XML "head" tag."""
    head_tag = doc.createElement('head')
    xmlTag = read_xml(table_of_contents[1][1])
    head_tag = xmlTag.getElementsByTagName('head')[0]

    # Attach CSS tags
    if css_files:
        for css_file_path in css_files:
            css_tag = doc.createElement('link')
            css_tag.setAttribute('type', 'text/css')
            file = os.path.join(os.path.dirname(book_file), css_file_path)
            os.makedirs(os.path.dirname(file), exist_ok=True)
            copyfile(os.path.join(epub_dir, css_file_path), file)
            css_tag.setAttribute('href', css_file_path)
            css_tag.setAttribute('rel', 'stylesheet')
            head_tag.appendChild(css_tag)

    return head_tag

def create_body_section(doc, table_of_contents, contents):
    """Build the XML "body" tag."""

    body_tag = doc.createElement('body')

    # build a TOC menu
    header_tag = doc.createElement('div')
    header_content_tag = doc.createElement('div')
    header_tag.setAttribute('class', 'header')
    header_content_tag.setAttribute('class', 'header-cont')

    ul1_tag = doc.createElement('ul')
    ul1_tag.setAttribute('class', 'dropdown')
    li1_tag = doc.createElement('li')
    a1_tag = doc.createElement('a')
    a1_tag.setAttribute('href', '#')
    a1_tag.appendChild(doc.createTextNode('Contents'))
    li1_tag.appendChild(a1_tag)
    ul2_tag = doc.createElement('ul')
    ul2_tag.setAttribute('class', 'sub_menu')

    # add the links to each individual chapter to the TOC menu
    for chap in table_of_contents:
        li2_tag = doc.createElement('li')
        a2_tag = doc.createElement('a')
        if len(chap[2]) > 0:
            a2_tag.setAttribute('href', '#' + chap[2])
        else:
            a2_tag.setAttribute('href', '#' + chap[1] + chap[2])
        a2_tag.appendChild(doc.createTextNode(chap[0]))
        li2_tag.appendChild(a2_tag)
        ul2_tag.appendChild(li2_tag)

    li1_tag.appendChild(ul2_tag)

    ul1_tag.appendChild(li1_tag)
    header_content_tag.appendChild(ul1_tag)
    header_tag.appendChild(header_content_tag)
    body_tag.appendChild(header_tag)

    # now insert the chapters
    content_tag = doc.createElement('div')
    content_tag.setAttribute('class', 'content')

    # Fill in the content
    inserted_chapters = []
    for chap in contents:
        # insert anchor here
        anchor_tag = doc.createElement('a')
        anchor_tag.setAttribute('id', chap[0])
        content_tag.appendChild(anchor_tag)
        # now append all the <p>'s and so on
        for node in chap[1:]:
            content_tag.appendChild(node)
    body_tag.appendChild(content_tag)
    return body_tag

def process(epub_content):
    # Create a temporary directory to extract and process the EPUB content
    temp_dir = os.path.dirname(__file__)
    # with tempfile.TemporaryDirectory() as temp_dir:
    epub_file_path = os.path.join(temp_dir, "input.epub")
    with open(epub_file_path, "wb") as f:
        f.write(epub_content)

    # Call the main function to process the EPUB file
    output_html = os.path.join(temp_dir, "output")
    os.makedirs(output_html, exist_ok=True)

    epub_dir = os.path.join(temp_dir, "epub")
    process_epub(epub_file_path, epub_dir, output_html)

    # Compress the generated HTML files back into a ZIP archive
    with io.BytesIO() as output:
        with zipfile.ZipFile(output, "w") as zf:
            for root, _, files in os.walk(os.path.join(temp_dir, output_html)):
                for file in files:
                    file_path = os.path.join(root, file)
                    zf.write(file_path,
                             os.path.relpath(file_path, os.path.join(temp_dir, output_html)))

        zip_content = output.getvalue()

    os.remove(epub_file_path)
    shutil.rmtree(os.path.join(temp_dir, output_html))

    return zip_content

def extract_image_paths(html_file):
    image_paths = []
    with open(html_file, 'r') as f:
        html_content = f.read()
        soup = BeautifulSoup(html_content, 'html.parser')
        img_tags = soup.find_all('img')
        for img_tag in img_tags:
            src = img_tag.get('src')
            if src:
                image_paths.append(src)
        img_tags = soup.find_all('image')
        for img_tag in img_tags:
            src = img_tag.attrs["xlink:href"]
            if src:
                image_paths.append(src)
    return image_paths

def process_epub(epub_file, epub_dir, output_html):
    # Extract files from the epub
    os.makedirs(epub_dir)
    subprocess.call(["unzip", "-qo", epub_file, "-d", epub_dir])

    # Extract information from the epub files
    opf_file = os.path.join(epub_dir, get_opf_file_path(epub_dir))
    root_dir = os.path.dirname(opf_file)

    toc_file = get_toc_file_path(opf_file)
    contents = extract_content(root_dir, opf_file)

    table_of_contents = get_chapter_list(epub_dir, root_dir, toc_file)

    book_file = os.path.join(output_html, "book.html")
    print(book_file)
    # lets hope all chapter files are in the same directory

    # Now build the new document
    doc = Document()
    html_tag = doc.createElement('html')
    doc.appendChild(html_tag)

    css_files = get_css_file_path_list(opf_file)

    html_tag.appendChild(
        create_head_section(doc, css=None, jquery=None, dropdown=None,
                            table_of_contents=table_of_contents,
                            epub_dir=root_dir, book_file=book_file, css_files=css_files))
    html_tag.appendChild(create_body_section(doc, table_of_contents, contents))

    output_dir = os.path.dirname(book_file)
    os.makedirs(output_dir, exist_ok=True)

    # write DOM to file
    with open(book_file, 'w') as f:
        f.write(doc.toprettyxml(encoding='utf-8').decode('utf-8'))

    image_paths = extract_image_paths(book_file)
    for image_path in image_paths:
        file = os.path.join(os.path.dirname(book_file), image_path)
        os.makedirs(os.path.dirname(file), exist_ok=True)
        shutil.copy(os.path.join(root_dir, image_path), file)

    # delete the old html files. they only take up space
    for chap in contents:
        try:
            os.remove(chap[0])
        except OSError:
            pass
    shutil.rmtree(epub_dir)


# Example usage:
# output_path = "./output.zip"
# path_to_epub = "./example.epub"
#
# with open(path_to_epub, "rb") as f:
#     epub_content_bytes = f.read()
#
# processed_content = process(epub_content_bytes)
#
# with open(output_path, "wb") as output_file:
#     output_file.write(processed_content)
# print("Обработанный контент был успешно записан в", output_path)
