document.getElementById('registrarPaciente').addEventListener('click', function() {
    let nombre = document.querySelector('input[name="nombre"]').value;
    let apellido = document.querySelector('input[name="apellido"]').value;
    let dni = document.querySelector('input[name="dni"]').value;
    let fechaIngreso = document.querySelector('input[name="fecha-ingreso"]').value;
    let calle = document.querySelector('input[name="calle"]').value;
    let numero = document.querySelector('input[name="numero"]').value;
    let localidad = document.querySelector('input[name="localidad"]').value;
    let provincia = document.querySelector('input[name="provincia"]').value;


        if (!nombre || !apellido || !dni || !fechaIngreso || !calle || !numero || !localidad || !provincia) {
            alert("Todos los campos son obligatorios.");
        }

        if (!/^\d{8}$/.test(dni)) {
            alert("El DNI debe ser un número de 8 dígitos.");
            return;
        }

        if (isNaN(numero) || numero < 1) {
            alert("El número de calle debe ser un valor numérico positivo.");
            return;
        }

  registrarPaciente(nombre, apellido, fechaIngreso,  calle, numero,  localidad, provincia,dni);
});

const registrarPaciente = (nombre, apellido, fechaIngreso,  calle, numero,  localidad, provincia, dni  ) => {
    const paciente = {
        nombre: nombre,
        apellido: apellido,
        fechaIngreso: fechaIngreso,
        dni: parseInt(dni),
        domicilioEntradaDto: {
            calle: calle,
            numero: parseInt(numero),
            localidad: localidad,
            provincia: provincia,
        },
    };
    fetch('http://localhost:8081/pacientes/registrar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(paciente),
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);


    })
    .catch((error) => {
        console.error('Error:', error);

    });

    let pacienteRegistrado = document.createElement('div');
pacienteRegistrado.innerHTML = `
<div class="card">
    <div class="card-body">
        <h5 class="card-title">${nombre} ${apellido}</h5>
        <p class="card-text">DNI: ${dni}</p>
        <p class="card-text">Fecha de ingreso: ${fechaIngreso}</p>
        <p class="card-text">Domicilio: ${calle} ${numero}, ${localidad}, ${provincia}</p>



    </div>
</div>
`;
document.getElementById('pacientesListados').appendChild(pacienteRegistrado);
document.querySelector('input[name="nombre"]').value = '';
document.querySelector('input[name="apellido"]').value = '';
document.querySelector('input[name="dni"]').value = '';
document.querySelector('input[name="fecha-ingreso"]').value = '';
document.querySelector('input[name="calle"]').value = '';
document.querySelector('input[name="numero"]').value = '';
document.querySelector('input[name="localidad"]').value = '';
document.querySelector('input[name="provincia"]').value = '';


}


document.getElementById('registarOdontologo').addEventListener('click', function() {
    let nombre = document.getElementById('nombreOdontologo').value;
    let apellido = document.getElementById('apellidoOdontologo').value;
    let matricula = document.getElementById('matricula').value;

      registrarOdontologo(nombre, apellido, matricula);
   });
   const registrarOdontologo = (nombre, apellido, matricula) => {
       const odontologo = {
           nombre: nombre,
           apellido: apellido,
           matricula: matricula
       };
       fetch('http://localhost:8081/odontologos/registrar', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json',
           },
           body: JSON.stringify(odontologo),
       })
       .then(response => response.json())
       .then(data => {
           console.log('Success:', data);
       })
       .catch((error) => {
           console.error('Error:', error);
       });

       let odontologoRegistrado = document.createElement('div');
       odontologoRegistrado.innerHTML = `
       <div class="card">
           <div class="card-body">
               <h5 class="card-title">${nombre} ${apellido}</h5>
               <p class="card-text">Matricula: ${matricula}</p>

           </div>
       </div>
       `;
       document.getElementById('odontologosListados').appendChild(odontologoRegistrado);
       document.getElementById('nombreOdontologo').value = '';
       document.getElementById('apellidoOdontologo').value = '';
       document.getElementById('matricula').value = '';

   }