(() => {
    const cards = document.querySelectorAll('.card');

    cards.forEach((card) => {
        let visibleRow = card.querySelector('#visible-row');
        let hiddenRow = card.querySelector('#hidden-row');
        let visibleRowMarginY = parseFloat(getComputedStyle(visibleRow).marginTop) + parseFloat(getComputedStyle(visibleRow).marginBottom);
        let hiddenRowMarginY = parseFloat(getComputedStyle(hiddenRow).marginTop) + parseFloat(getComputedStyle(hiddenRow).marginBottom);

        let dropdownToggle = card.querySelector('.card-dropdown-toggle');

        card.style.height = `${visibleRow.offsetHeight + visibleRowMarginY}px`;
        card.style.maxHeight = `${visibleRow.offsetHeight + visibleRowMarginY + hiddenRow.offsetHeight + hiddenRowMarginY}px`;
        dropdownToggle.onclick = () => {
            if(dropdownToggle.dataset.toggled === 'false'){
                // open card
                // card.style.removeProperty('height');
                card.style.height = card.style.maxHeight;
                // card.classList.toggle('active');
                dropdownToggle.dataset.toggled = 'true';
            } else {
                // close card
                card.classList.toggle('active');
                card.style.height = `${visibleRow.offsetHeight + visibleRowMarginY}px`;
                card.style.maxHeight = `${visibleRow.offsetHeight + visibleRowMarginY + hiddenRow.offsetHeight + hiddenRowMarginY}px`;
                dropdownToggle.dataset.toggled = 'false';
            }
        }
    });
})()



