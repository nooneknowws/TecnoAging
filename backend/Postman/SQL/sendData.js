const data = require('./DATA.json');

const API_URL_PACIENTE = 'http://localhost:3000/api/pacientes';
const API_URL_TECNICO = 'http://localhost:3000/api/tecnicos';
const pacientesData = data.pacientes;
const tecnicosData = data.tecnicos;

async function sendData() {
  console.log('Starting to process pacientes...');
  for (const paciente of pacientesData) {
    try {
      const response = await fetch(API_URL_PACIENTE, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(paciente),
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      console.log(`Successfully sent ${paciente.nome}: ${response.status}`);
    } catch (error) {
      console.error(`Error sending ${paciente.nome}:`, error.message);
    }
    
    await new Promise(resolve => setTimeout(resolve, 1000));
  }
  console.log('All patients processed');
  console.log('Starting to process tecnicos...');
   for (const tecnico of tecnicosData) {
    try {
      const response = await fetch(API_URL_TECNICO , {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(tecnico),
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      console.log(`Successfully sent ${tecnico.nome}: ${response.status}`);
    } catch (error) {
      console.error(`Error sending ${tecnico.nome}:`, error.message);
    }
    
    await new Promise(resolve => setTimeout(resolve, 1000));
  }
  console.log('All tecnicos processed');
}

sendData();