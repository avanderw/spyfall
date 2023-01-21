package main

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/gobwas/ws"
	"github.com/gobwas/ws/wsutil"
	"github.com/patrickmn/go-cache"
)

func main() {
	fmt.Println("Server started, waiting for connection on port 8080...")
	http.ListenAndServe(":8080", http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		conn, _, _, err := ws.UpgradeHTTP(r, w)
		fmt.Println(r.RemoteAddr, "connected")
		if err != nil {
			fmt.Println("Error upgrading connection:", err)
			return
		}
		go func() {
			defer conn.Close()
			for {
				req, op, err := wsutil.ReadClientData(conn)
				if err != nil {
					for _, game := range gameCache.Items() {
						g := game.Object.(Game)
						for i, c := range g.conns {
							if (*c).RemoteAddr().String() == r.RemoteAddr {
								var player Player
								for j, p := range g.Players {
									if (*p.conn).RemoteAddr().String() == r.RemoteAddr {
										player = g.Players[j]
										g.Players = append(g.Players[:j], g.Players[j+1:]...)
										break
									}
								}
								g.conns = append(g.conns[:i], g.conns[i+1:]...)
								gameCache.Set(g.ID, g, cache.DefaultExpiration)

								msg := RequestMessage{Command: "close", PlayerID: player.ID}
								m, err := json.Marshal(ResponseMessage{Request: msg, Game: g})
								if err != nil {
									fmt.Println("Error marshalling message:", err)
									return
								}
								notifyAll(g, m)
								break
							}
						}
					}
					return
				}

				fmt.Println("Received:", string(req))
				resp := route(conn, req)
				err = wsutil.WriteServerMessage(conn, op, resp)
				fmt.Println("Sent:", string(resp))
				if err != nil {
					fmt.Println("Error writing message:", err)
					return
				}
			}
		}()
	}))
}
