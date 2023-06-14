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
        json_message = json.loads(message)
        if json_message["request"] == "POST":
            if json_message["session"] in SESSIONS:
                for i in SESSIONS[json_message["session"]]["users"]:
                    await SESSIONS[json_message["session"]]["users"][i].send(message)
        elif json_message["request"] == "DISCONNECT":
            del SESSIONS[json_message["session"]]["users"][
                SESSIONS[json_message["session"]]["users"].index(json_message["id"])
            ]
            del SESSIONS[json_message["session"]]["users"][
                SESSIONS[json_message["session"]]["users_names"].index(
                    json_message["id"]
                )
            ]
            SESSIONS[json_message["session"]]["cnt"] -= 1
            if SESSIONS[json_message["session"]]["cnt"] == 0:
                del SESSIONS[json_message["session"]]
        elif json_message["request"] == "CONNECT":
            if json_message["session"] in SESSIONS:
                if not SESSIONS[json_message["session"]]["start"]:
                    for i in SESSIONS[json_message["session"]]["users"]:
                        await websocket.send(
                            f"{{"
                            f'"session":"{json_message["session"]}",'
                            f'"request":"PLAYER_ADDED",'
                            f'"username":"{SESSIONS[json_message["session"]]["users_names"][i]}"'
                            f"}}"
                        )
                    SESSIONS[json_message["session"]]["cnt"] += 1
                    SESSIONS[json_message["session"]]["users"][
                        SESSIONS[json_message["session"]]["cnt"]
                    ] = websocket
                    SESSIONS[json_message["session"]]["users_names"][
                        SESSIONS[json_message["session"]]["cnt"]
                    ] = json_message["username"]
                    await websocket.send(
                        f"{{"
                        f'"session":{json_message["session"]},'
                        f'"request":"CONNECT", '
                        f'"data":{SESSIONS[json_message["session"]]["cnt"]}'
                        f"}}"
                    )
                    for i in SESSIONS[json_message["session"]]["users"]:
                        await SESSIONS[json_message["session"]]["users"][i].send(
                            f"{{"
                            f'"session":"{json_message["session"]}",'
                            f'"request":"PLAYER_ADDED",'
                            f'"username":"{json_message["username"]}"'
                            f"}}"
                        )
                else:
                    await websocket.send(
                        f"{{"
                        f'"session":"{json_message["session"]}",'
                        f'"request":"CONNECT", '
                        f'"data":0'
                        f"}}"
                    )
            else:
                await websocket.send(
                    f"{{"
                    f'"session":"{json_message["session"]}",'
                    f'"request":"CONNECT", '
                    f'"data":0'
                    f"}}"
                )
        elif json_message["request"] == "ARMOR" or json_message["request"] == "ATTACK":
            await SESSIONS[json_message["session"]]["users"][
                json_message["id_target"]
            ].send(json_message)
        elif json_message["request"] == "START":
            SESSIONS[json_message["session"]]["start"] = True
            for i in SESSIONS[json_message["session"]]["users"]:
                await SESSIONS[json_message["session"]]["users"][i].send(
                    "{" '"request":"START"' "}"
                )
        elif json_message["request"] == "CREATE":
            if not (json_message["session"] in SESSIONS):
                SESSIONS[json_message["session"]] = {
                    "cnt": 1,
                    "start": False,
                    "users": {1: websocket},
                    "users_names": {1: json_message["username"]},
                }
                await websocket.send(
                    f"{{"
                    f'"session":"{json_message["session"]}",'
                    f'"request":"CREATE", '
                    f'"data":1'
                    f"}}"
                )
                await websocket.send(
                    f"{{"
                    f'"session":"{json_message["session"]}",'
                    f'"request":"PLAYER_ADDED",'
                    f'"username":"{json_message["username"]}"'
                    f"}}"
                )
            else:
                await websocket.send(
                    f"{{"
                    f'"session":"{json_message["session"]}",'
                    f'"request":"CREATE",'
                    f'"data":0'
                    f"}}"
                )


async def main():
    async with websockets.serve(handler, "0.0.0.0", 8001):
        await asyncio.Future()


asyncio.run(main())
