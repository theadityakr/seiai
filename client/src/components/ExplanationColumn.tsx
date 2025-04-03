import React, { useContext } from 'react';
import ReactMarkdown from 'react-markdown';
import { WidgetContext } from '../services/RealTimeUpdates';

const ExplanationColumn: React.FC = () => {
  const { currentWidget } = useContext(WidgetContext);

  return (
    <div className="column explanation">
      <h2>Explanation</h2>
      {currentWidget ? (
        <ReactMarkdown>{currentWidget.explanation}</ReactMarkdown>
      ) : (
        <ReactMarkdown>No explanation available yet</ReactMarkdown>
      )}
    </div>
  );
};

export default ExplanationColumn;