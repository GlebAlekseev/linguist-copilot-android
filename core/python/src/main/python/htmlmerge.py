import os
import re
import sys
import zipfile
import shutil

from bs4 import BeautifulSoup


def process_merge_html(html_zip_path, html_input_path, output_html):
    with zipfile.ZipFile(html_zip_path, 'r') as zip_ref:
        zip_ref.extractall(html_input_path)
    infile = os.path.join(html_input_path, "book.html")
    try:
        with open(infile, 'r') as f:
            indexHTML = f.read()
    except:
        sys.exit(1)

    dom = BeautifulSoup(indexHTML, features="lxml")

    # merge js
    if True:
        # find all script tags in html
        scripts = dom.find_all('script')

        # string containing merged scripts
        all_scripts = ""

        # iterate over script tags
        for tag in scripts:
            # get src url of script tag
            if tag.has_key("src"):
                # save source url to variable
                src_url = tag['src']
            else:
                # append content of script tag to string
                all_scripts += ''.join(tag.contents)
                continue
            # end if

            # read src to src
            try:
                with open(os.path.join(os.path.dirname(infile), src_url), 'r') as f:
                    all_scripts += f.read()
            except:
                sys.exit(1)
            # end try

            # remove src attribute
            tag.extract()
        # end for

        # create script tag for all scripts
        scripts_tag = dom.new_tag("script")

        # inset merged scripts into new script tag
        scripts_tag.insert(0, all_scripts)

        # get head tag
        head = dom.find('head')

        # append scripts tag to head
        head.insert(len(head.contents), scripts_tag)
    # end if

    # merge css
    if True:
        # find all link tags in html
        links = dom.find_all('link')

        # string containing merged stylesheets
        all_css = ""

        # iterate over link tags
        for tag in links:
            # get content of rel attribute
            if tag.has_key("rel"):
                rel = " ".join(tag['rel'])

                if rel == "stylesheet" and tag.has_key("href"):
                    # get href url of link tag
                    src_url = tag['href']

                    # read src to src
                    try:
                        with open(os.path.join(os.path.dirname(infile), src_url), 'r') as f:
                            all_css += f.read()
                    except:
                        sys.exit(1)
                    # end try

                    # remove src attribute
                    tag.extract()
                else:
                    continue
            else:
                continue
        # end for

        # find all style tags in html
        styles = dom.find_all('style')

        # iterate over style tags
        for tag in styles:
            all_css += ''.join(tag.contents)
        # end for

        # merge_css_import
        if True:
            # search for @import url(*);
            re_url = re.compile(r"@import\s+url\(['\"]?(.+?)['\"]?\);")

            # while mergable @import exist
            while re_url.search(all_css):
                all_css = all_css.split("\n")
                for line in all_css:
                    if len(re_url.findall(line)) > 0:
                        src_url = re_url.findall(line)[0]  # get src url

                        idx = all_css.index(line)  # get @import index
                        all_css.pop(idx)  # remove @import

                        # read src to src
                        try:
                            with open(src_url, 'r') as f:
                                # insert loaded css at old @import index
                                all_css.insert(idx, f.read())
                        except:
                            sys.exit(1)
                        # end try
                    # end if
                # end for
                all_css = '\n'.join(all_css)
            # end while
        # end if

        # create style tag for all css files
        style_tag = dom.new_tag("style")

        # inset merged css into new style tag
        style_tag.insert(0, all_css)

        # get head tag
        head = dom.find('head')
        # append scripts tag to head
        head.insert(len(head.contents), style_tag)
    # end if

    all_html = dom.prettify()
    try:
        with open(output_html, 'w') as f:
            f.write(all_html)
    except Exception as e:
        print("Ошибка при записи в файл:", e)
        sys.exit(1)


def process(html_zip):
    temp_dir = os.path.dirname(__file__)
    html_zip_path = os.path.join(temp_dir, "input.zip")
    html_input_path = os.path.join(temp_dir, "input")
    os.makedirs(html_input_path, exist_ok=True)
    with open(html_zip_path, "wb") as f:
        f.write(html_zip)
    output_html = os.path.join(temp_dir, "output3.html")
    process_merge_html(html_zip_path, html_input_path, output_html)
    with open(output_html, "rb") as html_file:
        html_content = html_file.read()
    os.remove(html_zip_path)
    shutil.rmtree(html_input_path)

    os.remove(output_html)
    return html_content
