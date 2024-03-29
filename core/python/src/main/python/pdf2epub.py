import os
import re
import sys

from PyPDF2 import PdfFileReader
from ebooklib import epub
from pdfminer.converter import HTMLConverter, TextConverter
from pdfminer.layout import LAParams
from pdfminer.pdfdocument import PDFDocument
from pdfminer.pdfinterp import PDFResourceManager, PDFPageInterpreter
from pdfminer.pdfpage import PDFPage
from pdfminer.pdfparser import PDFParser
from tqdm import tqdm
import zipfile
import io
import shutil

if not sys.warnoptions:
    import warnings

    warnings.simplefilter('ignore')


def extract_information(pdf_path):
    with open(pdf_path, 'rb') as f:
        pdf = PdfFileReader(f)
        information = pdf.getDocumentInfo()
        number_of_pages = pdf.getNumPages()

    txt = f'''
    Information about {pdf_path}: 

    Author: {information.author}
    Creator: {information.creator}
    Producer: {information.producer}
    Subject: {information.subject}
    Title: {information.title}
    Number of pages: {number_of_pages}
    '''

    print(txt)
    return information


def get_page_text(pdf_path, n):
    with open(pdf_path, 'rb') as f:
        pdf = PdfFileReader(f)
        text = pdf.getPage(n).extractText()
    return text


def get_page_content(pdf_path, n):
    with open(pdf_path, 'rb') as f:
        pdf = PdfFileReader(f)
        text = pdf.getPage(n).getContents()
    return text


def get_all_raw_text(pdf_path):
    with open(pdf_path, 'rb') as f:
        pdf = PdfFileReader(f)
        n = pdf.getNumPages()
        buffer = ''
        for i in range(1, n):
            buffer += pdf.getPage(i).extractText()
    return buffer


def get_pdfs():
    cwd = os.getcwd()
    os.chdir(cwd + os.sep + 'pdfs')
    pdfs = filter(lambda f: f[-4:] == '.pdf', os.listdir())
    os.chdir(cwd)
    return pdfs


def set_folder(folder):
    current = os.getcwd() + os.sep + folder
    os.mkdir(current) if not os.path.isdir(current) else ''


def default_folders():
    set_folder('pdfs')
    set_folder('texts')
    set_folder('epubs')
    set_folder('htmls')


def pdf2text(fn, output):
    # default_folders()
    caching = True
    rsrcmgr = PDFResourceManager(caching=caching)
    laparams = LAParams()
    laparams.all_texts = True
    imagewriter = None
    outfp = open(output, 'w', encoding='utf-8')
    device = TextConverter(rsrcmgr, outfp, laparams=laparams,
                           imagewriter=imagewriter)
    with open(fn, 'rb') as fp:
        interpreter = PDFPageInterpreter(rsrcmgr, device)
        pagenos = set()
        maxpages = 0
        password = b''
        rotation = 0
        for page in PDFPage.get_pages(fp, pagenos,
                                      maxpages=maxpages, password=password,
                                      caching=caching, check_extractable=False):
            page.rotate = (page.rotate + rotation) % 360
            interpreter.process_page(page)
    device.close()
    outfp.close()


def clean_texts(fn):
    current = os.getcwd() + os.sep + 'cleaned'
    os.mkdir(current) if not os.path.isdir(current) else ''
    with open(fn, 'r', encoding='utf-8') as fi:
        with open('cleaned' + os.sep + fn, 'w', encoding='utf-8') as fo:
            prev = ''
            for l in fi.readlines():
                line = l.strip()
                if line == '':
                    line = '\n'
                if len(prev) < 70:
                    line += '\n'
                fo.write(line)
                prev = line


def pdf2html(fn, output):
    # default_folders()
    caching = True
    rsrcmgr = PDFResourceManager(caching=caching)
    laparams = LAParams()
    laparams.all_texts = True
    imagewriter = None

    result = io.StringIO()
    with open(output, 'w', encoding='utf-8') as outfp:
        device = HTMLConverter(rsrcmgr, result, scale=1,
                               layoutmode='normal', laparams=laparams,
                               imagewriter=imagewriter, debug=0)
        with open(fn, 'rb') as fp:
            interpreter = PDFPageInterpreter(rsrcmgr, device)
            pagenos = set()
            maxpages = 0
            password = b''
            rotation = 0
            for page in PDFPage.get_pages(fp, pagenos,
                                          maxpages=maxpages, password=password,
                                          caching=caching, check_extractable=False):
                page.rotate = (page.rotate + rotation) % 360
                interpreter.process_page(page)
        device.close()
        bytes = result.getvalue()
        # outfp.write(bytes.decode('utf-8'))
        outfp.write(bytes)
        outfp.close()
        return bytes


def create_epub_metadata(book, metadata):
    book.set_identifier(metadata.autor + metadata.title)
    book.set_title(metadata.title)
    book.set_language('en')
    book.add_author(metadata.author)
    if metadata.subject != None:
        book.add_metadata('DC', 'description', metadata.subject)
    if metadata.creator != None:
        book.add_metadata(None, 'meta', '', {
            'name': 'creator', 'content': metadata.creator})
    if metadata.producer != None:
        book.add_metadata(None, 'meta', '', {
            'name': 'producer', 'content': metadata.producer})


def add_html_to_epub(book, html):
    c1 = epub.EpubHtml(title='Main content',
                       file_name='main.xhtml',
                       lang='en')
    with open(html, 'r', encoding='utf-8') as f:
        c1.set_content(f.read())
    book.toc = (epub.Link('main.xhtml', 'Main content', 'main'),
                (
                    epub.Section('Languages'),
                    (c1,)
                )
                )
    book.spine = ['nav', c1]
    book.add_item(epub.EpubNcx())
    book.add_item(epub.EpubNav())
    book.add_item(c1)
    return book


def create_epub_metadata_from_pdf(book, pdf):
    fp = open(pdf, 'rb')
    parser = PDFParser(fp)
    doc = PDFDocument(parser)
    metadata = doc.info[0]
    print('\n' * 2, metadata, '\n' * 2)
    try:
        book.set_title(metadata['Title'])
        book.set_language('en')
    except KeyError:
        print('Title not found in {}'.format(pdf))
    try:
        book.add_author(metadata['Author'])
    except KeyError:
        print('Author not found in {}'.format(pdf))
    try:
        book.set_identifier(metadata['Author'] + metadata['Title'])
    except KeyError:
        pass
    try:
        book.add_metadata(None, 'meta', '', {
            'name': 'creator', 'content': metadata['Creator']})
    except KeyError:
        print('Creator not found in {}'.format(pdf))
    try:
        book.add_metadata(None, 'meta', '', {
            'name': 'producer', 'content': metadata['Producer']})
    except KeyError:
        print('Producer not found in {}'.format(pdf))
    try:
        book.add_metadata(None, 'meta', '', {
            'name': 'creationDate', 'content': metadata['CreationDate']})
    except KeyError:
        print('CreationDate not found in {}'.format(pdf))
    try:
        book.add_metadata(None, 'meta', '', {
            'name': 'Keywords', 'content': metadata['Keywords']})
    except KeyError:
        print('Keywords not found in {}'.format(pdf))
    return book


def strip_extension(f):
    return '.'.join(f.split('.')[:-1])


def clean_html(fn):
    current = os.getcwd() + os.sep + 'cleaned'
    os.mkdir(current) if not os.path.isdir(current) else ''
    with open(fn, 'r', encoding='utf-8') as fi:
        with open('cleaned' + os.sep + fn, 'w', encoding='utf-8') as fo:
            new_html, n = re.subn('<div style=".*;">', '', fi.read())
            # TODO! clean html from untag </div>
            new_html, n = re.subn('border:[^;]*;', '', new_html)
            new_html, n = re.subn('</div>', '', new_html)
            print('\nCleaning {} from div style. substituted {} times.'.format(fn[4:], n))
            # new_html, n = re.subn('writing-mode:lr-tb;', '', new_html)
            # print('\nCleaning {} from writing-mode:lr-tb;. substituted {} times.'.format(fn[4:], n))
            fo.write(new_html)


def cd_create(folder):
    os.mkdir(folder) if not os.path.isdir(folder) else ''
    os.chdir(folder)

def process(pdf_content):
    # Create a temporary directory to extract and process the EPUB content
    temp_dir = os.path.dirname(__file__)
    # with tempfile.TemporaryDirectory() as temp_dir:
    pdf_file_path = os.path.join(temp_dir, "input.pdf")
    pdf_file_output = os.path.join(temp_dir, "output")
    os.makedirs(pdf_file_output, exist_ok=True)
    with open(pdf_file_path, "wb") as f:
        f.write(pdf_content)
        pdf_file_name = os.path.basename(pdf_file_path)
        file_name = strip_extension(pdf_file_name)
        txt_file_name = file_name + '.txt'
        pdf2text(pdf_file_path, os.path.join(pdf_file_output, txt_file_name))

        book = create_epub_metadata_from_pdf(epub.EpubBook(), pdf_file_path)
        html_file_name = file_name + '.html'
        html_file_path = os.path.join(pdf_file_output, html_file_name)
        pdf2html(pdf_file_path, html_file_path)

        add_html_to_epub(book, html_file_path)

        epub_file_name = file_name + '.epub'
        epub_file_path = os.path.join(pdf_file_output, epub_file_name)

        epub.write_epub(epub_file_path, book)

        # Compress the generated HTML files back into a ZIP archive
        with open(epub_file_path, "rb") as epub_file:
            epub_content = epub_file.read()

        os.remove(pdf_file_path)
        shutil.rmtree(pdf_file_output)

        return epub_content

#
# # Example usage:
# output_path = "./outputPdf.zip"
# path_to_epub = "./pdfs/jcompose.pdf"
#
# with open(path_to_epub, "rb") as f:
#     pdf_content_bytes = f.read()
#
# processed_content = process(pdf_content_bytes)
#
# with open(output_path, "wb") as output_file:
#     output_file.write(processed_content)
# print("Обработанный контент был успешно записан в", output_path)



# if __name__ == '__main__':
#     file_paths = tqdm(get_pdfs())
#     succes = 0
#     fail = 0
#
#     for path in file_paths:
#         file_paths.set_description('pdf2text: {:<20}'.format(path))
#         raw_fn = strip_extension(path)
#         fo = raw_fn + '.txt'
#         try:
#             pdf2text('pdfs' + os.sep + path, fo)
#             cwd = os.getcwd()
#             cd_create(cwd + os.sep + 'texts')
#             try:
#                 file_paths.set_description(
#                     'Cleaning text: {:<20}'.format(path))
#                 clean_texts(fo)
#             except UnicodeEncodeError as err:
#                 print('\n' * 2 + '*' * 5 +
#                       'UnicodeEncodeError cleaning file: ' + path + '\n' * 2)
#                 print(err)
#             os.chdir(cwd)
#             book = create_epub_metadata_from_pdf(
#                 epub.EpubBook(), 'pdfs' + os.sep + path)
#             html_path = raw_fn + '.html'
#             pdf2html('pdfs' + os.sep + path, html_path)
#
#             cd_create(cwd + os.sep + 'htmls')
#             try:
#                 file_paths.set_description(
#                     'Cleaning html: {:<20}'.format(path))
#                 clean_html(html_path)
#             except UnicodeEncodeError as err:
#                 print('\n' * 2 + '*' * 5 +
#                       'UnicodeEncodeError cleaning file: ' + path + '\n' * 2)
#                 print(err)
#             os.chdir(cwd)
#
#             add_html_to_epub(book, 'htmls' + os.sep +
#                              'cleaned' + os.sep + html_path)
#             cd_create(cwd + os.sep + 'epubs')
#             epub.write_epub(raw_fn + '.epub', book)
#             os.chdir(cwd)
#             succes += 1
#         except UnicodeDecodeError as err:
#             print('\n' * 2 + '*' * 5 +
#                   'Error executing pdf2text with file: ' + path + '\n' * 2)
#             print(err)
#             fail += 1
#     print('\nSuccess: {} Fail: {}'.format(succes, fail))
