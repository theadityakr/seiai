// src/components/RealTimeUpdates.tsx
import React, { useEffect, useState, createContext } from 'react';
import { webSocketService } from '../services/WebSocketService';

interface Widget {
  ipAddress: string;
  explanation: string;
  code: string;
  extra: string;
}

interface WidgetContextType {
  currentWidget: Widget | null;
  lastUpdated: string;
}

export const WidgetContext = createContext<WidgetContextType>({
  currentWidget: null,
  lastUpdated: ''
});

const RealTimeUpdates: React.FC<{children: React.ReactNode}> = ({ children }) => {
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
    <WidgetContext.Provider value={{ currentWidget, lastUpdated }}>
      {children}
    </WidgetContext.Provider>
  );
};

export default RealTimeUpdates;