import Place from "../Place";

function Column(props) {
    const { column: { cards }, columnId } = props;
    let lineId = 0;

    return (
        <div key={`column-${columnId}`} style={{display: "flex", flexDirection: "column"}}>
            {
                cards.map(c => <Place key={`columnplace-${columnId}-${lineId}`} card={c} columnId={columnId} lineId={lineId++} />)
            }
        </div>
    );
}

export default Column;