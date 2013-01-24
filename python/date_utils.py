#!/usr/bin/env python

from datetime import datetime

today = datetime.today()
print today.day
print today
milliseconds = (today.day * 24 * 60 * 60 + today.second) * 1000 + today.microsecond / 1000.0
print milliseconds
