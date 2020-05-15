import re
import os
import sys

nytimes_com = '''
This New Liquid Is Magnetic, and Mesmerizing

Scientists have created “soft” magnets that can flow 
and change shape, and that could be a boon to medicine 
and robotics. (Source: New York Times)


Most Wikipedia Profiles Are of Men. This Scientist Is Changing That.

Jessica Wade has added nearly 700 Wikipedia biographies for
 important female and minority scientists in less than two 
 years.

'''

bloomberg_com = '''
The Space Race: From Apollo 11 to Elon Musk

It's 50 years since the world was gripped by historic images
 of Apollo 11, and Neil Armstrong -- the first man to walk 
 on the moon. It was the height of the Cold War, and the charts
 were filled with David Bowie's Space Oddity, and Creedence's 
 Bad Moon Rising. The world is a very different place than 
 it was 5 decades ago. But how has the space race changed since
 the summer of '69? (Source: Bloomberg)


Twitter CEO Jack Dorsey Gives Talk at Apple Headquarters

Twitter and Square Chief Executive Officer Jack Dorsey 
 addressed Apple Inc. employees at the iPhone maker’s headquarters
 Tuesday, a signal of the strong ties between the Silicon Valley giants.
'''


def check_url(url):
    p = re.compile(r"([a-z]{3})?\w*\.\[a-z]{3}", re.IGNORECASE)
    return p.match(url) is not None

def get_url():
    url = ""
    while True:
        usr_url = input()
        if usr_url == "exit":
            break;
        if check_url(usr_url):
            if usr_url == "bloomberg.com":
                url = bloomberg_com
                break
            elif usr_url == "nytimes.com":
                url = nytimes_com
                break
            else:
                print("page not found")
        else:
            print("Error: Incorrect URL")
    return url

# write your code here
def main():
    dir = ""
    if len(sys.argv) >= 2:
        dir = sys.argv[1]
        dir = "./" + dir
        os.mkdir(dir)

    url = get_url()

    print(url)
    if url != "":
        file_name = url[:-4]
        with open(dir + "/" + file_name, "w") as file:
            file.write(url)

     # write user read file

     # file not found

if __name__ == '__main__':
    main()
