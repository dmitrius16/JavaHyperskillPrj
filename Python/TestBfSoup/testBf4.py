from bs4 import BeautifulSoup

with open("test.html","r") as f:
    content = "".join(f.readlines())
    soup = BeautifulSoup(content, 'html.parser')
    print(soup.prettify())