import requests
import sys
import os
from collections import deque
import re

def get_url(url_str):
    r = re.compile("https?://")
    if r.match(url_str) is None:
        return "https://"+url_str
    return url_str

def is_file(usr_input_str):
    return '.' not in usr_input_str[::-1]

def main():
    dir = ""
    page_story = deque()
    if len(sys.argv) >= 2:
        dir = sys.argv[1]
        dir_list = os.listdir()
        if dir not in dir_list:
            os.mkdir(dir)
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
                    with open(dir + "/" + usr_inp, "r") as file:
                        for line in file:
                            print(line)
                except IOError:
                    print("file not found")
            else:
                url = get_url(usr_inp)
                content = requests.get(url)
                print(content.text)
                if url_content_prev != "":
                    page_story.append(url_content_prev)
                url_content_prev = content.text
                file_name = usr_inp[:-4]
                with open(dir + "\\" + file_name, "w") as file:
                    file.write(content.text)


if __name__ == "__main__":
    main()

# write your code here
