$(document).ready(function() {
    function setActiveLink() {
        var currentPath = window.location.pathname;

        currentPath = currentPath.startsWith('/') ? currentPath.slice(1) : currentPath;

        $('.nav-item a').each(function() {
            let $this = $(this),
                href = $this.attr('href'),
                hrefPath = href.startsWith('/') ? href.slice(1) : href;

            if (href === '/' && currentPath === '') {
                $this.addClass('active');
            } else if (hrefPath === currentPath ) {
                $this.addClass('active');
            } else {
                $this.removeClass('active');
            }
        });
    }

    setActiveLink();

    $(window).on('popstate', setActiveLink);
});