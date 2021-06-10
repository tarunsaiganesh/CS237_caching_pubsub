import csv
from random import seed
from random import randint
import sys

size = int(sys.argv[1])

pubID = [0]*size
restaurant = [0]*size
food_item = [0]*size
discount = [0]*size
val = [0]*size

validSubs = []
validSubs.append("10001")
validSubs.append("00110")
#validSubs.append("11000")

#print(data)
for j in range(1,4):
  seed(randint(0,1000))
  data = [0]*size
  for i in range(size):
    pubID[i] = randint(0,1)
    val[i] = validSubs[randint(0,1)]
    data[i]=[pubID[i],val[i]]
  file="/home/mason/Documents/CS237_caching_pubsub/test{benchmark}_{tmp}.csv".format(benchmark=size,tmp=j)
  with open(file, 'w') as f:
    writer = csv.writer(f)
    writer.writerows(data)

