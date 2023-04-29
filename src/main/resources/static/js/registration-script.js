(() => {
   let toggler = document.querySelector("#button");
   let firstTab = document.querySelector("#first-tab");
   let secondTab = document.querySelector("#second-tab");

   toggler.addEventListener("click", () => {
       firstTab.classList.toggle("show");
       firstTab.classList.toggle("active");

       secondTab.classList.toggle("show");
       secondTab.classList.toggle("active");

       window.scrollTo({top:0, behavior: "smooth"});
   });

   let pesel = document.querySelector("#pesel");
   pesel.value = "";
   pesel.textContent = "";
})()