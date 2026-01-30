import { FormattedMessage, useIntl } from 'react-intl';

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

//Form input fields
export const NameInput = () => {
    return (
        <div className="form-group">
            <label className='secondary-text' name="name"><FormattedMessage id="forum.form.username" /></label>
            <input type="text"
                name="name"
                placeholder={namePlaceholder()} />
        </div>
    )
}
export const PasswordInput = () => {
    return(
    <div className="form-group">
        <label className='secondary-text' name="password"><FormattedMessage id="forum.form.password" /></label>
        <input type="password"
            name="password"
            placeholder={passwordPlaceholder()}
        />
    </div>
    )
}
export const ConfirmPasswordInput = () => {
    return(
        <div className="form-group">
          <label className='secondary-text' name="confirmPassword"><FormattedMessage id="forum.form.confirmPassword" /></label>
          <input type="password"
            name="confirmPassword"
            placeholder={confirmPasswordPlaceholder()} />
        </div>
    )
}