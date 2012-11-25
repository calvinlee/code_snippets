#!/usr/bin/python

import sys
import os
import shutil

def main():
    if len(sys.argv) < 3:
        print "Tell me the root dir of source tree and destination dir"
        return

    print "Counting repos..."
    os.unlink("/tmp/repos")
    os.system("repo forall -c 'echo $REPO_PATH' >> /tmp/repos")
    repos = open("/tmp/repos", "r")
    for repo in repos:
        print ">>>>>>>>>> walking repo ", repo
        walkDir(repo.strip('\n'), sys.argv[2])

    os.unlink("/tmp/repos")
    print "Done"

def walkDir(rootDir, dstDir):
    exclude_prefix = ('.', 'tests')

    for file in os.listdir(rootDir):
        if file.startswith(exclude_prefix):
            continue
        path = os.path.join(rootDir, file)

        if os.path.isdir(path):
            walkDir(path, dstDir)

        if file == 'strings.xml':
            copyto = dstDir + "/" + rootDir
            os.makedirs(copyto)
            shutil.copy(path, copyto)
            print "found string file:", path

if __name__ == '__main__':
    main()
