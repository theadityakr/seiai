// src/services/WebSocketService.ts
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

interface SubscriptionCallback {
  (message: any): void;
}

interface Subscriptions {
  [destination: string]: SubscriptionCallback;
}

class WebSocketService {
  private client: Client;
  private subscriptions: Subscriptions = {};
  private stompSubscriptions: { [destination: string]: StompSubscription } = {};

  constructor() {
    this.client = new Client({
      webSocketFactory: () => new SockJS('https://4854-38-126-136-103.ngrok-free.app/ws'),
      debug: (str: string) => console.log(str),
      reconnectDelay: 1000,
      heartbeatIncoming: 1000,
      heartbeatOutgoing: 1000,
    });

    this.client.onConnect = (frame) => {
      console.log('Connected to WebSocket', frame);
      Object.entries(this.subscriptions).forEach(([destination, callback]) => {
        this.stompSubscriptions[destination] = this.client.subscribe(
          destination,
          (message: IMessage) => {
            try {
              callback(JSON.parse(message.body));
            } catch (error) {
              console.error('Error parsing message:', error);
              callback(message.body);
            }
          }
        );
      });
    };

    this.client.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.client.onWebSocketError = (error) => {
      console.error('WebSocket error:', error);
    };

    this.client.onDisconnect = () => {
      console.log('Disconnected from WebSocket');
    };
  }

  public connect(): void {
    if (!this.client.active) {
      this.client.activate();
    }
  }

  public disconnect(): void {
    if (this.client.active) {
      this.client.deactivate();
    }
  }

  public subscribe(destination: string, callback: SubscriptionCallback): void {
    this.subscriptions[destination] = callback;

    if (this.client.connected) {
      this.stompSubscriptions[destination] = this.client.subscribe(
        destination,
        (message: IMessage) => {
          try {
            callback(JSON.parse(message.body));
          } catch (error) {
            console.error('Error parsing message:', error);
            callback(message.body);
          }
        }
      );
    }
  }

  public unsubscribe(destination: string): void {
    delete this.subscriptions[destination];
    if (this.stompSubscriptions[destination]) {
      this.stompSubscriptions[destination].unsubscribe();
      delete this.stompSubscriptions[destination];
    }
  }
}

export const webSocketService = new WebSocketService();
