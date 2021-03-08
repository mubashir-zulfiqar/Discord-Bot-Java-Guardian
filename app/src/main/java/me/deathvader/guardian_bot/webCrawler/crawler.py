# Developer: Mubashir Zulfiqar  Date: 2/27/2021  Time: 5:04 AM
# Happy Coding...
# https://www.megalobiz.com/search/all?qry=DHARIA+-+Sugar+%26+Brownies+(by+Monoir)+[Official+Video]&searchButton.x=0&searchButton.y=0

# import os
# import requests
# from bs4 import BeautifulSoup
# from sys import argv


# def spider(link_url):
#     source_code = requests.get(link_url)
#     plain_text = source_code.text
#     soup = BeautifulSoup(plain_text, features="html.parser")
#     href = ''
#
#     for link in soup.findAll('a', {'class': 'entity_name'}):
#         href = 'https://www.megalobiz.com' + link.get('href')
#         break
#
#     get_single_item_data(href)
#
#
# def get_single_item_data(link_url):
#     source_code = requests.get(link_url)
#     plain_text = source_code.text
#     soup = BeautifulSoup(plain_text, features="html.parser")
#     file = open("lyrics.lrc", "w")
#     for link in soup.findAll('div', {'class', 'lyrics_details entity_more_i nfo'}):
#         file.write(link.text)
#     file.close()
#
#
# f = open("link.txt", "r")
# url = f.read()
# f.close()
#
# if os.path.exists("link.txt"):
#     os.remove("link.txt")  # one file at a time
#
# spider(url)
