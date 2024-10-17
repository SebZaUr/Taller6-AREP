document.addEventListener('DOMContentLoaded', () =>{
    const form = document.getElementById("siguInForm");
    form.addEventListener("submit",siguIn);
    const url = "http://localhost:8080/arep/seguridad";

    async function siguIn(event) {
        event.preventDefault();
        const email = document.getElementById('correo').value;
        const password = document.getElementById('contrasena').value;
        const confirmText= document.getElementById('confirm');
        try{
            const res = await fetch(url,{
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({email, password})
            });
            const data = await res.json();
            if (res.ok) {
                const data = await res.json();
                confirmTextElement.textContent = "Pudo ingresar satisfactoriamente";
            } else {
                confirmTextElement.textContent = "No pudo ingresar correctamente";
            }
            form.reset();
        }catch(error){
            alert('Error: ' + error)
        }
    }
})