import { FormattedMessage } from 'react-intl';
export const CancelButton = ({ setVisible }) => {
    return <button type="button" onClick={() => setVisible(prev => !prev)} className="submit-btn cancel">
        <FormattedMessage id="forum.form.cancel" />
        </button>
};