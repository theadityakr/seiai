// src/components/RealTimeUpdates.tsx
import React, { useEffect, useState } from 'react';
import { webSocketService } from '../services/WebSocketService';

interface Widget {
  ipAddress: string;
  explanation: string;
  code: string;
  extra: string;
}

const RealTimeUpdates: React.FC = () => {
  const [currentWidget, setCurrentWidget] = useState<Widget | null>(null);
  const [lastUpdated, setLastUpdated] = useState<string>('');

  useEffect(() => {
    webSocketService.connect();
    
    webSocketService.subscribe('/topic/widget-updates', (widget: Widget) => {
      setCurrentWidget(widget);
      setLastUpdated(new Date().toLocaleTimeString());
    });

    return () => {
      webSocketService.disconnect();
    };
  }, []);

  return (
    <div className="real-time-updates">
      <h3>Real-time Widget Updates</h3>
      {currentWidget ? (
        <div className="widget-item">
          <div className="widget-header">
            <h4>IP: {currentWidget.ipAddress}</h4>
            <span className="update-time">Updated at: {lastUpdated}</span>
          </div>
          <div className="widget-content">
            <div className="widget-section">
              <h5>Explanation</h5>
              <p>{currentWidget.explanation}</p>
            </div>
            <div className="widget-section">
              <h5>Code</h5>
              <pre><code>{currentWidget.code}</code></pre>
            </div>
            <div className="widget-section">
              <h5>Extra</h5>
              <p>{currentWidget.extra}</p>
            </div>
          </div>
        </div>
      ) : (
        <p>Waiting for widget updates...</p>
      )}
    </div>
  );
};

export default RealTimeUpdates;