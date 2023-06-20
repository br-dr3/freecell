import Place from "../Place";

function Foundations(props) {
    const { clubs, hearts, spades, diamonds } = props.cards;

    return (
        <div className="foundations" style={ {
            display: "flex",
            flexDirection: "row",
        } } >
            <Place card={clubs.cards.at(-1)} foundation />
            <Place card={hearts.cards.at(-1)} foundation />
            <Place card={diamonds.cards.at(-1)} foundation />
            <Place card={spades.cards.at(-1)} foundation />
        </div>
    );
}

export default Foundations;