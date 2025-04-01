import React, { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

const WebSocketComponent = () => {
    const [data, setData] = useState<string[]>([]);

    useEffect(() => {
        const stompClient = new Client({
            brokerURL: "ws://localhost:8080/ws",
            onConnect: () => {
                console.log("Connected to WebSocket");

                stompClient.subscribe("/topic/data", (message) => {
                    setData((prevData) => [...prevData, message.body]);
                });
            },
            onStompError: (frame) => {
                console.error("WebSocket Error: ", frame);
            },
        });

        stompClient.activate();

        return () => {
            stompClient.deactivate().catch((error) => 
                console.error("WebSocket Deactivation Error:", error)
            );
        };
    }, []);

    return (
        <div>
            <h2>Real-Time Database Updates</h2>
            <ul>
                {data.map((item, index) => (
                    <li key={index}>{item}</li>
                ))}
            </ul>
        </div>
    );
};

export default WebSocketComponent;
