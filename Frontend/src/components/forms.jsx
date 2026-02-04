import { FormattedMessage, useIntl } from 'react-intl';
import { useFormData } from '../global-variables/FormData.jsx';

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
    const { formData, setFormData } = useFormData();
    return (
        <div className="form-group">
            <label className='secondary-text' name="name"><FormattedMessage id="forum.form.username" /></label>
            <input type="text"
                name="name"
                placeholder={namePlaceholder()}
                value={formData.name}
                onChange={(e) => setFormData({...formData, [e.target.name]: e.target.value})}
            />
        </div>
    )
}
export const PasswordInput = () => {
    const { formData, setFormData } = useFormData();
    return (
        <div className="form-group">
            <label className='secondary-text' name="password"><FormattedMessage id="forum.form.password" /></label>
            <input type="password"
                name="password"
                placeholder={passwordPlaceholder()}
                value={formData.password}
                onChange={(e) => setFormData({...formData, [e.target.name]: e.target.value})}
            />
        </div>
    )
}
export const ConfirmPasswordInput = () => {
    const { formData, setFormData } = useFormData();
    return (
        <div className="form-group">
            <label className='secondary-text' name="confirmPassword"><FormattedMessage id="forum.form.confirmPassword" /></label>
            <input type="password"
                name="confirmPassword"
                placeholder={confirmPasswordPlaceholder()}
                value={formData.confirmPassword}
                onChange={(e) => setFormData({...formData, [e.target.name]: e.target.value})}
            />
        </div>
    )
}