import csv
from random import seed
from random import randint

size = 1000
pubID = [0]*size
restaurant = [0]*size
food_item = [0]*size
discount = [0]*size
data = [0]*size
val = [0]*size

seed(27)
for i in range(1000):
    pubID[i] = randint(0,1)
    restaurant[i] = bin(pubID[i])[2:].zfill(1)
    food_item[i] = bin(randint(0,3))[2:].zfill(2)
    discount[i] = bin(randint(0,2))[2:].zfill(2)
    val[i] = restaurant[i]+food_item[i]+discount[i]
    data[i] = [pubID[i],val[i]]
#print(data)
with open('/home/mason/Documents/CS237_caching_pubsub/test.csv', 'w') as f:
  writer = csv.writer(f)
  writer.writerows(data)

