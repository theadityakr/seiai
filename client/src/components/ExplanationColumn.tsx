import React, { useContext } from 'react';
import { WidgetContext } from '../services/RealTimeUpdates';

const ExplanationColumn: React.FC = () => {
  const { currentWidget } = useContext(WidgetContext);

  return (
    <div className="column explanation">
      <h2>Explanation</h2>
      {currentWidget ? (
        <p>{currentWidget.explanation}</p>
      ) : (
        <p>No explanation available yet</p>
      )}
    </div>
  );
};

export default ExplanationColumn;