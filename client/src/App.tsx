import React from "react";
import "./App.css";
import ExplanationColumn from "./components/ExplanationColumn";
import CodeBlockColumn from "./components/CodeBlockColumn";
import ExtraContentColumn from "./components/ExtraContentColumn";
import RealTimeUpdates from "./services/RealTimeUpdates";

function App() {
  return (
    <>
    <RealTimeUpdates/>
    <div className="main colflex">
      <h3>SEI AI</h3>
      <div className="container rowflex">
        <ExplanationColumn />
        <CodeBlockColumn />
        <ExtraContentColumn />
      </div>
    </div>
    </>
  );
}

export default App;
