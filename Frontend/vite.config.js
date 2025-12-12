import { defineConfig } from 'vite'
import basicSsl from '@vitejs/plugin-basic-ssl'
import react from '@vitejs/plugin-react'

//TODO: delete basicSsl() if not used on localhost
// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), basicSsl()],
})
