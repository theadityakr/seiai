import React, { useContext } from 'react';
import { WidgetContext } from '../services/RealTimeUpdates';

const ExtraContentColumn: React.FC = () => {
  const { currentWidget } = useContext(WidgetContext);

  return (
    <div className="column extra-content">
      <h2>Extra Content</h2>
      {currentWidget ? (
        <p>{currentWidget.extra}</p>
      ) : (
        <p>No extra content available yet</p>
      )}
    </div>
  );
};

export default ExtraContentColumn;