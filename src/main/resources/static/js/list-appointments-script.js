(() => {
    const cards = document.querySelectorAll('.card');

    cards.forEach((card) => {
        let visibleRow = card.querySelector('#visible-row');
        let visibleRowMarginY = parseFloat(getComputedStyle(visibleRow).marginTop) + parseFloat(getComputedStyle(visibleRow).marginBottom);
        let hiddenRow = card.querySelector('#hidden-row');
        let hiddenRowMarginY = parseFloat(getComputedStyle(hiddenRow).marginTop) + parseFloat(getComputedStyle(hiddenRow).marginBottom);

        let dropdownToggle = card.querySelector('.card-dropdown-toggle');
        let toggleArrow = card.querySelector('.card-dropdown-toggle i');

        card.style.height = `${visibleRow.offsetHeight + visibleRowMarginY}px`;
        card.style.maxHeight = `${visibleRow.offsetHeight + visibleRowMarginY + hiddenRow.offsetHeight + hiddenRowMarginY}px`;
        dropdownToggle.onclick = (event) => {
            event.preventDefault();
            if(dropdownToggle.dataset.toggled === 'false'){
                // open card
                toggleArrow.style.transform = 'rotate(90deg)';
                card.style.height = card.style.maxHeight;
                dropdownToggle.dataset.toggled = 'true';
            } else {
                // close card
                toggleArrow.style.transform = 'rotate(0deg)';
                card.style.height = `${visibleRow.offsetHeight + visibleRowMarginY}px`;
                card.style.maxHeight = `${visibleRow.offsetHeight + visibleRowMarginY + hiddenRow.offsetHeight + hiddenRowMarginY}px`;
                dropdownToggle.dataset.toggled = 'false';
            }
        }
    });
})()



