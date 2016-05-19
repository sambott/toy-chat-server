### Sample Python Client
#
# start with Python 3.5 environment
# install modules with `pip install requests websocket-client argparse`
#
# run `python chat.py --room test`

import argparse
import websocket
import threading
import time
import requests
import json
import datetime


URL_ROOT = 'http://localhost:9000'

def getRecentMessages(room):
  url = '{}/rooms/{}'.format(URL_ROOT, room);
  msgs = requests.get(url)
  return json.loads(msgs.text)

def printMessage(msg):
  dt_int = msg['dateTime']
  user = msg['user']
  msgBody = msg['message']
  dt = datetime.datetime.fromtimestamp(dt_int / 1000)
  print('[{}] {}  -  {}'.format(dt.strftime("%Y-%m-%d %H:%M:%S"), user, msgBody))

def on_message(ws, message):
  printMessage(json.loads(message))

def on_error(ws, error):
  print(error)

def on_close(ws):
  print('### closed ###')

def send_some_messages(ws):
  def run(*args):
    for i in range(3):
      time.sleep(1)
      send_msg = {'user': 'demo user', 'message': 'Hello {}'.format(i)}
      ws.send(json.dumps(send_msg))
    ws.close()
    print('demo chat thread terminating...')
  listen_thread = threading.Thread(target=run, daemon=False)
  listen_thread.start()

def pollRoom(room):
  url = 'ws://{}/rooms/{}/ws'.format(URL_ROOT.split('://')[1], room)
  ws = websocket.WebSocketApp(url, on_message = on_message,
      on_error = on_error, on_close = on_close)
  ws.on_open = send_some_messages
  ws.run_forever()

if __name__ == "__main__":
  parser = argparse.ArgumentParser(description='Connect to a chat room.')
  parser.add_argument('--room', required=True, help='Name of room to conenct to')
  args = parser.parse_args()

  recentMsgs = getRecentMessages(args.room)
  for m in recentMsgs:
    printMessage(m)

  pollRoom(args.room)

