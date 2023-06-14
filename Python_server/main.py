import asyncio
import json

import websockets

SESSIONS = dict()


async def handler(websocket):
    global SESSIONS
    while True:
        try:
            message = await websocket.recv()
        except websockets.ConnectionClosedOK:
            break
        mess = json.loads(message)
        if mess["request"] == "POST":
            if mess["session"] in SESSIONS:
                for i in SESSIONS[mess["session"]]["users"]:
                    await SESSIONS[mess["session"]]["users"][i].send(message)
        elif mess["request"] == "DISCONNECT":
            del SESSIONS[mess["session"]]["users"][
                SESSIONS[mess["session"]]["users"].index(mess["id"])
            ]
            del SESSIONS[mess["session"]]["users"][
                SESSIONS[mess["session"]]["users_name"].index(mess["id"])
            ]
            SESSIONS[mess["session"]]["cnt"] -= 1
            if SESSIONS[mess["session"]]["cnt"] == 0:
                del SESSIONS[mess["session"]]
        elif mess["request"] == "CONNECT":
            if mess["session"] in SESSIONS:
                if not SESSIONS[mess["session"]]["start"]:
                    SESSIONS[mess["session"]]["cnt"] += 1
                    SESSIONS[mess["session"]]["users"][
                        SESSIONS[mess["session"]]["cnt"]
                    ] = websocket
                    SESSIONS[mess["session"]]["users_name"][
                        SESSIONS[mess["session"]]["cnt"]
                    ] = mess["username"]
                    await websocket.send(
                        f"{{"
                        f'"session":{mess["session"]},'
                        f'"request":"CONNECT", '
                        f'"data":{SESSIONS[mess["session"]]["cnt"]}'
                        f"}}"
                    )
                    for i in SESSIONS[mess["session"]]["users"]:
                        await SESSIONS[mess["session"]]["users"][i].send(
                            f"{{"
                            f'"session":"{mess["session"]}",'
                            f'"request":"PLAYER_ADDED",'
                            f'"username":"{mess["username"]}"'
                            f"}}"
                        )
                else:
                    await websocket.send(
                        f"{{"
                        f'"session":"{mess["session"]}",'
                        f'"request":"CONNECT", '
                        f'"data":0'
                        f"}}"
                    )
            else:
                await websocket.send(
                    f"{{"
                    f'"session":"{mess["session"]}",'
                    f'"request":"CONNECT", '
                    f'"data":0'
                    f"}}"
                )
        elif mess["request"] == "ARMOR" or mess["request"] == "ATTACK":
            await SESSIONS[mess["session"]]["users"][mess["id_target"]].send(mess)
        elif mess["request"] == "START":
            SESSIONS[mess["session"]]["start"] = True
            for i in SESSIONS[mess["session"]]["users"]:
                await SESSIONS[mess["session"]]["users"][i].send('{'
                                                                 '"request":"START"'
                                                                 '}')
        elif mess["request"] == "CREATE":
            if not (mess["session"] in SESSIONS):
                SESSIONS[mess["session"]] = {
                    "cnt": 1,
                    "start": False,
                    "users": {1: websocket},
                    "users_name": {1: mess["username"]}
                }
                await websocket.send(
                    f"{{"
                    f'"session":"{mess["session"]}",'
                    f'"request":"CREATE", '
                    f'"data":1'
                    f"}}"
                )
                await websocket.send(
                    f"{{"
                    f'"session":"{mess["session"]}",'
                    f'"request":"PLAYER_ADDED",'
                    f'"username":"{mess["username"]}"'
                    f"}}"
                )
            else:
                await websocket.send(
                    f"{{"
                    f'"session":"{mess["session"]}",'
                    f'"request":"CREATE", '
                    f'"data":0'
                    f"}}"
                )


async def main():
    async with websockets.serve(handler, "0.0.0.0", 8001):
        await asyncio.Future()


asyncio.run(main())
