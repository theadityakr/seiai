import React, { useContext } from 'react';
import ReactMarkdown from 'react-markdown';
import { WidgetContext } from '../services/RealTimeUpdates';

const ExtraContentColumn: React.FC = () => {
  const { currentWidget } = useContext(WidgetContext);

  return (
    <div className="column extra-content">
      <h2>Extra Content</h2>
      {currentWidget ? (
        <ReactMarkdown>{currentWidget.extra}</ReactMarkdown>
      ) : (
        <ReactMarkdown>No extra content available yet</ReactMarkdown>
      )}
    </div>
  );
};

export default ExtraContentColumn;