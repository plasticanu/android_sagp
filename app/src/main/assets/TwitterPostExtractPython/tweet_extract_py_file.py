import os
import tweepy as tw
import xml.etree.ElementTree as ET
import numpy as np
import pickle

consumer_key = "V49B6qBjE2x3YWyKqgq0tOklZ"
consumer_secret = "jm4SrEiidBalOqRI7D5IEghz56rvEGpcgscvRoyYrbnaPCnmtB"
access_key = "1522366452384161792-QODP9Czf4xgRaz2UnXKhuhtikXEChE"
access_secret = "khDtX6g1QGMdGe8cZNYOaqXwDNJ2zgXLBGnuJEHPcnj9d"

auth = tw.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_key, access_secret)
api = tw.API(auth, wait_on_rate_limit=True)

# Define the search term and the date_since date as variables
search_words = ['University', 'ANU', 'Sport', 'Australia', 'ANU campus', 
                'food', 'Assignment', 'Exams', 'Mental health', 'University students', 
                'University Research', 'Computer sciense', 'Natrual science', 'Student life', 'Music', 
                'Jazz music', 'Pop music', 'Software engineer', 'Canberra', 'Car', 
                'Basketball', 'Football', 'Rugby', 'handball', 'Athletics', 
                'Drawing', 'Anime', 'Japanese Anime', 'Love', 'Crush']

# Collect tweets
print('start searching key words')
i = 1
tweets = []
for sw in search_words:
    tweets.append(tw.Cursor(api.search_tweets,
              q='123',
              lang="en").items(100))
    print(str(i) + " key word searched", end = '\r')
    i += 1
print('', end = '\r')
print('tweets extract done')
# flatten the list

i = 1
flat_list = []
for sublist in tweets:
    print('flattening the list ' + str(i), end = '\r')
    i += 1
    o = 0
    for item in sublist:
        print(str(o) + ' item processed', end = '\r')
        flat_list.append(item)
        o += 1
print('', end = '\r')
tweets = flat_list
tweets_data= []
print('flattening done')

print('Start getting statuses')
i = 1;
for t in tweets:
    try:
        tweets_data.append(api.get_status(t.id, tweet_mode="extended"))
        print("Getting tweet statuses: "+str(i) , end='\r')
        i += 1
    except Exception:
        pass
print('', end = '\r')
print('tweet data has been obtained')
i = 1
tweet_list = []
for td in tweets_data:
    data = [td.user.screen_name, #author
      td.full_text, #content
      td.favorite_count, #likes_count
      td.author.created_at] # Time
    tweet_list.append(data)
    print("Getting detailed data: "+str(i) , end='\r')
    i += 1
print('', end = '\r')
print("detailed data has been obtained")

i = 1
with open('tweets_data', 'wb') as fp:
    pickle.dump(tweets_data, fp)

with open ('tweets_data', 'rb') as fp:
    tweet_list = pickle.load(fp)
print(tweet_list[0].full_text)




#pretty print method
def indent(elem, level=0):
    i = "\n" + level*"  "
    j = "\n" + (level-1)*"  "
    if len(elem):
        if not elem.text or not elem.text.strip():
            elem.text = i + "  "
        if not elem.tail or not elem.tail.strip():
            elem.tail = i
        for subelem in elem:
            indent(subelem, level+1)
        if not elem.tail or not elem.tail.strip():
            elem.tail = j
    else:
        if level and (not elem.tail or not elem.tail.strip()):
            elem.tail = j
    return elem

#root element
root = ET.Element('Post', {})

post_count = 1
#post sub-element
for tl in tweet_list:
    post = ET.SubElement(root, "post", {'id' : str(post_count)})
    post_count = post_count + 1
    
    author = ET.SubElement(post, 'author')
    content = ET.SubElement(post, 'content')
    likes_count = ET.SubElement(post, 'likes_count')
    time = ET.SubElement(post, 'time')
    comment = ET.SubElement(post, 'comment')
    author.text = str(tl.user.screen_name)
    content.text = str(tl.full_text)
    likes_count.text = str(tl.favorite_count)
    time.text = str(tl.author.created_at)

#write to file
tree = ET.ElementTree(indent(root))
tree.write('posts.xml', xml_declaration=True, encoding='utf-8')