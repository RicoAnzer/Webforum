import { useState, createContext, useContext } from 'react';

//FormData saves formData in Sign Up and Login Forms and prevents reset of input field if errors occur
const FormDataContext = createContext({
    formData: {
        name: '',
        password: '',
        confirmPassword: ''
    }, setFormData: () => { }
});
export const useFormData = () => useContext(FormDataContext);

export const FormDataProvider = ({ children }) => {
    const [formData, setFormData] = useState({
        name: '',
        password: '',
        confirmPassword: ''
    });
    return (
        <FormDataContext.Provider value={{ formData, setFormData }}>
            {children}
        </FormDataContext.Provider>
    )
}