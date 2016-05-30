import requests

url = 'https://en.wikipedia.org/w/api.php?action=query&format=json&pageids=37397829&generator=links&gpllimit=max'
r = requests.get(url)

gplcontinue = r.json()['continue']['gplcontinue']

data = r.json()['query']['pages']

while gplcontinue:
	url = 'https://en.wikipedia.org/w/api.php?action=query&format=json&pageids=37397829&generator=links&gpllimit=max&gplcontinue=' + gplcontinue
	r = requests.get('https://en.wikipedia.org/w/api.php?action=query&format=json&pageids=37397829&generator=links&gpllimit=max&gplcontinue=' + gplcontinue)
	newdata = r.json()['query']['pages']
	data.update(newdata)
	if 'continue' in r.json():
		gplcontinue = r.json()['continue']['gplcontinue']
		print 'gplcontinue: ' + gplcontinue
	else:
		break

print data
with open('top5000.txt', 'w') as f:
	for article in data:
		f.write(str(article) + ",")
