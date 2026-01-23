import { useCallback, useContext, React } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import App from '../../App.jsx';

export function useSignUp() {
    //useNavigate() allows navigation to other pages
    const navigate = useNavigate();
    const header = {
        'Content-Type': 'application/json'
    }
    return async (formData) => {
        const name = formData.get("name");
        const userPassword = formData.get("password");
        const confirmUserPassword = formData.get("confirmPassword");
        await axios
            .post("https://localhost:8080/auth/register", { username: name, password: userPassword, confirmedPassword: confirmUserPassword }, {
                withCredentials: true,
                headers: header
            }).then(response => {
                navigate('/login');
            })
            .catch(error => {
                console.log(error);
            });
    }
}

export function useLogin() {
    //useNavigate() allows navigation to other pages
    const navigate = useNavigate();
    const header = {
        'Content-Type': 'application/json'
    }
    return async (formData) => {
        const varname = formData.get('name');
        const varpassword = formData.get('password');
        await axios
            .post("https://localhost:8080/auth/login", { username: varname, password: varpassword }, {
                withCredentials: true,
                headers: header
            }).then(response => {
                console.log(response.data);
                navigate('/');
            })
            .catch(error => {
                console.log(error);
            });
    }
}

export function useLogout() {
    //useNavigate() allows navigation to other pages
    const navigate = useNavigate();
    const header = {
        'Content-Type': 'application/json'
    }
    return useCallback(async () => {
        await axios
            .post("https://localhost:8080/auth/logout", {}, {
                //Send cookies along with request
                withCredentials: true,
                headers: header
            }).then(response => {
                //After logout send back to main page
                navigate('/login');
            })
            .catch(error => {
                console.log(error);
            });
    })
}