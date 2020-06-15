import requests
import sys
import os
from collections import deque
from bs4 import BeautifulSoup
import re

def get_url(url_str):
    r = re.compile("https?://")
    if r.match(url_str) is None:
        return "https://"+url_str
    return url_str

def is_file(usr_input_str):
    return '.' not in usr_input_str[::-1]


def main():
    usr_dir = ""
    page_story = deque()
    if len(sys.argv) >= 2:
        usr_dir = sys.argv[1]
        if not os.path.exists(usr_dir):
            os.mkdir(usr_dir)
    url_content_prev = ""
    while True:
        usr_inp = input()
        if usr_inp == "exit":
            break
        elif usr_inp == "back":
            if len(page_story) > 0:
                url_content = page_story.pop()
                print(url_content)
        else:
            if is_file(usr_inp):
                try:
                    with open(usr_dir + "/" + usr_inp, "r") as file:
                        for line in file:
                            print(line)
                except IOError:
                    print("file not found")
            else:
                url = get_url(usr_inp)
                page = requests.get(url)
                soup = BeautifulSoup(page.content, 'html.parser')
                tags = soup.find_all(['p', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'a', 'ul', 'ol', 'li'])
                content = []
                for tag in tags:
                    if len(tag.text) and tag.text[0] == '\n':
                        continue
                    content.append(tag.text)
                content = "\n".join(content)
                print(content)
                # print(content.text)
                if url_content_prev != "":
                    page_story.append(url_content_prev)
                url_content_prev = content
                file_name = usr_inp[:-4]
                with open(usr_dir + "\\" + file_name, "w") as file:
                    file.write(content)


if __name__ == "__main__":
    main()

# write your code here
