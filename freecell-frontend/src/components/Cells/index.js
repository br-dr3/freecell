import Place from "../Place";

function Cells(props) {
    const { cards } = props;

    return (
        <div className="cells" key="cells" style={{display: "flex", flexDirection: "row"}}>
            {
                Array.from({length: 4}, (v, k) => k).map(i => <Place key={`cellplace-${i}`} card={cards[i]} cell />)
            }
        </div>
    );
}

export default Cells;