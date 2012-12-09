#!/usr/bin/env python
# Calvin.Lee<lihao921@gmail.com> @ Sat Dec  8 22:52:12 CST 2012

import os
import re
import urllib
import urllib2

def main():
    #file = codecs.open('/home/calvin/Desktop/playmusic.php', 'r', 'utf-8')
    p = re.compile(r"http://[\da-z/.]*mp3")
    songuri = []
    file = open('/home/calvin/Desktop/playmusic.php')
    for line in file:
        m = p.search(line)
        if m:
            songuri.append(m.group())

    print len(songuri)

    for url in songuri:
        #print url
        download(url, os.path.basename(url))

def download(url, localFileName):
    print "downloading from %s..." % url
    headers = {
        'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6',
        'Referer':'http://www.songtaste.com/'
    }

    request = urllib2.Request(
        url = url,
        headers = headers
    )
    response = urllib2.urlopen(request).read()
    print response

    #urllib.urlretrieve(url, localFileName)

if __name__ == "__main__":
    main()
