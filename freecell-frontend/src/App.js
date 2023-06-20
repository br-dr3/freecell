import React, { useEffect, useState } from "react";
import Foundations from "./components/Foundations";
import Cells from "./components/Cells";
import Column from "./components/Column"

import './App.css';

function App() {
    
    const [ game, setGame ] = useState({cardsDistribution: null});

    const [ { width, height }, setWidthAndHeight ] = useState({
        width: window.innerWidth,
        height: window.innerHeight
    });

    useEffect(
        () => () => {
            setWidthAndHeight({
                width: window.innerWidth,
                height: window.innerHeight
            });    
        }, []
    );

    useEffect(() => {
        setWidthAndHeight({
            width: window.innerWidth,
            height: window.innerHeight
        });

        function loadGame() {

            fetch("http://localhost:12000/games/1")
            .then(response => response.json())
            .then(json => {
                setGame(json.data);
            });
        }

        loadGame();
    }, []);

    let columnId = 0;

    return (
        game.cardsDistribution !== null? 
            <div className="container" style = {{width, height}} key="container">
                <div className="firstLine" key="first-line"style={
                        {
                            display: "flex", 
                            flexDirection: "row",
                            width: "100%"
                        }
                    }
                >
                    <Foundations cards={game.cardsDistribution.foundation} />
                    <Cells cards={game.cardsDistribution.cells.cards} />
                </div>
                <br/>
                <br/>
                <div key="matrix" style={{display: "flex", flexDirection: "row"}}>
                    {
                        game.cardsDistribution.matrix.columns.map(
                            c => <Column key={`column-${columnId}`} column={c} columnId={columnId++} />
                        )
                    }
                </div>
            </div>
        :
            <div className="container" key="container"/>
    );
}

export default App;