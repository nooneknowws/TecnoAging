const pacientesData = require('./pacientes.json');

const API_URL = 'http://localhost:3000/api/pacientes';

async function sendPacientes() {
  for (const paciente of pacientesData.pacientes) {
    try {
      const response = await fetch(API_URL, {
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
}

sendPacientes();