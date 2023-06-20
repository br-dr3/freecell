import './place.css';


function Place(props) {
    const { card, columnId, lineId, cell, foundation } = props;

    const renderEmptySpace = card === undefined || card === null;

    const onClickByColor = {
        "white": select,
        "lightblue": unselect,
        "transparent": () => {}
    }
    
    const changeColor = {"white": "lightblue", "lightblue": "white"}
    const changeColorSuits = {
        "CLUBS": ["HEARTS", "DIAMONDS"], 
        "HEARTS": ["CLUBS", "SPADES"],
        "SPADES": ["HEARTS", "DIAMONDS"], 
        "DIAMONDS": ["CLUBS", "SPADES"],
    }

    if(renderEmptySpace) {
        return <div className="place" style={{ backgroundColor: "transparent" }}/>
    }

    function getAllCards() {
        return [...document.body.getElementsByClassName("card")]
        .map(c => {
            return {
                card: c, 
                column: c.getAttribute("column"), 
                line: c.getAttribute("line"),
                color: c.style.backgroundColor,
                order: c.getAttribute("order")
            }
        });
    }

    function allCardsInColumn(columnId) {
        return getAllCards().filter(c => c.column === columnId);
    }

    function unselectAllOtherColumns(columnId) {
        getAllCards().filter(c => c.column !== columnId).forEach(c => c.card.style.backgroundColor = "white");
    }

    function select(e) {
        let column = e.target.getAttribute("column");
        let line = e.target.getAttribute("line");

        if(cell) {
            e.target.style.backgroundColor = "lightblue";
            return;
        }

        if(foundation) {
            return;
        }

        unselectAllOtherColumns(column);

        let cardsInColumn = allCardsInColumn(column);

        let firstSelected = cardsInColumn.find(c => c.color === 'lightblue');
        let firstLineToSelect = firstSelected === undefined || firstSelected === null? line : Math.min(line, firstSelected.line); 

        let cardsToSelect = cardsInColumn.slice(0, cardsInColumn.length -1).filter((c, i) => {

            let isSameColumn = c.column === column
            let isLineBigger = c.line >= firstLineToSelect
            
            let isOrdered = parseInt(cardsInColumn[i+1].order) + 1 === parseInt(c.order);

            console.log(`cardsInColumn[i+1].order + 1 === c.order => ${(cardsInColumn[i+1].order + 1)} + 1 === ${c.order}`)


            return isSameColumn && isLineBigger && isOrdered;
        }).concat(cardsInColumn.slice(-1));

        cardsToSelect.forEach(c => c.card.style.backgroundColor = "lightblue");
    }

    function unselect(e) {
        const color = "lightblue";

        if(cell) {
            e.target.style.backgroundColor = "white";
            return;
        }

        if(foundation) {
            return;
        }

        let column = e.target.getAttribute("column");
        let line = e.target.getAttribute("line");

        let cardsInColumn = allCardsInColumn(column, line);

        let firstUnselected = cardsInColumn.reverse().find(c => c.color === "white");
        let firstLineToUnselect = firstUnselected === undefined || firstUnselected === null? line : Math.max(line, firstUnselected.line);
        
        let cardsToUnselect = cardsInColumn.filter(c => c.column === column && c.line <= firstLineToUnselect);

        cardsToUnselect.forEach(c => {
            c.card.style.backgroundColor = changeColor[color];
        });
    }    

    const placeStyle = lineId > 0 ? {marginTop: "-50px"} : {}
    
 
    return (
            <div className="place" key={`place-${card.id}`} style={{...placeStyle}}>
                <button column={columnId} line={lineId} 
                    key={`card-${card.id}`} className="card" style={{backgroundColor: "white"}}
                    onClick={(e) => onClickByColor[e.target.style.backgroundColor](e)} order={card.order}
                    cell={new Boolean(cell).toString()} foundation={new Boolean(foundation).toString()}>
                    {`${card.label}${card.symbol}`}
                </button>
            </div>
    );
}

export default Place;