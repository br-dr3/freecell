import Place from "../Place";

function Column(props) {
    const { column: { cards }, columnId } = props;

    if(cards.length === 0) {
        return <Place key={`columnplace-${columnId}`} columnId={columnId}/>;
    }

    let lineId = 0;

    return (
        <div key={`column-${columnId}`} style={{display: "flex", flexDirection: "column"}}>
            {
                cards.map((c, i) => 
                    <Place key={`columnplace-${columnId}-${lineId}`} card={c} columnId={columnId} lineId={i} />
                )
            }
        </div>
    );
}

export default Column;