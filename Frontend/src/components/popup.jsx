import { FormattedMessage, useIntl } from 'react-intl';
import { useNameInput, usePasswordInput, useConfirmPasswordInput, useAddTopicInput, useAddThreadInput } from '../global-variables/PopupData.jsx';

//Internationalization of placeholders
function namePlaceholder() {
    const intl = useIntl();
    return intl.formatMessage({
        id: 'forum.form.username.placeholder',
    });
}
function passwordPlaceholder() {
    const intl = useIntl();
    return intl.formatMessage({
        id: 'forum.form.password.placeholder',
    });
}
function confirmPasswordPlaceholder() {
    const intl = useIntl();
    return intl.formatMessage({
        id: 'forum.form.confirmPassword.placeholder',
    });
}
function addTopicPlaceholder() {
    const intl = useIntl();
    return intl.formatMessage({
        id: 'forum.form.addTopic.placeholder',
    });
}

function addThreadPlaceholder() {
    const intl = useIntl();
    return intl.formatMessage({
        id: 'forum.form.addThread.placeholder',
    });
}

//Form input fields
export const NameInput = () => {
    const { nameInput, setNameInput } = useNameInput();
    return (
        <div className="form-group">
            <label className='secondary-text' name="name"><FormattedMessage id="forum.form.username" /></label>
            <input type="text"
                name="name"
                placeholder={namePlaceholder()}
                value={nameInput}
                onChange={(e) => setNameInput(e.target.value)}
            />
        </div>
    )
}
export const PasswordInput = () => {
    const { passwordInput, setPasswordInput } = usePasswordInput();
    return (
        <div className="form-group">
            <label className='secondary-text' name="password"><FormattedMessage id="forum.form.password" /></label>
            <input type="password"
                name="password"
                placeholder={passwordPlaceholder()}
                value={passwordInput}
                onChange={(e) => setPasswordInput(e.target.value)}
            />
        </div>
    )
}
export const ConfirmPasswordInput = () => {
     const { confirmPasswordInput, setConfirmPasswordInput } = useConfirmPasswordInput();
    return (
        <div className="form-group">
            <label className='secondary-text' name="confirmPassword"><FormattedMessage id="forum.form.confirmPassword" /></label>
            <input type="password"
                name="confirmPassword"
                placeholder={confirmPasswordPlaceholder()}
                value={confirmPasswordInput}
                onChange={(e) => setConfirmPasswordInput(e.target.value)}
            />
        </div>
    )
}

export const AddTopicInput = () => {
    const { addTopicInput, setAddTopicInput } = useAddTopicInput();
    return (
        <div className="form-group">
            <label className='secondary-text' name="addTopic"><FormattedMessage id="forum.form.addTopic" /></label>
            <input type="text"
                name="addTopic"
                placeholder={addTopicPlaceholder()}
                value={addTopicInput}
                onChange={(e) => setAddTopicInput(e.target.value)}
            />
        </div>
    )
}

export const AddThreadInput = () => {
    const { addThreadInput, setAddThreadInput } = useAddThreadInput();
    return (
        <div className="form-group">
            <input type="text"
                name="addThread"
                placeholder={addThreadPlaceholder()}
                value={addThreadInput}
                onChange={(e) => setAddThreadInput(e.target.value)}
            />
        </div>
    )
}