import { FormattedMessage } from "react-intl";

function PluralComponent({ itemCount }) {
    return (
        <FormattedMessage
            id="itemCount"
            values={{ itemCount }}
        />
    )
}