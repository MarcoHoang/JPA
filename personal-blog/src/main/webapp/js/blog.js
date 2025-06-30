$(document).ready(function () {
    let offset = 0;
    const limit = 5;

    const $loadMoreBtn = $('#loadMoreBtn');
    const $blogList = $('#blogList');
    const $keywordInput = $('#keyword');

    $('#searchForm').on('submit', function (e) {
        e.preventDefault();
        const keyword = $keywordInput.val().trim();

        if (keyword === "") {
            offset = 0;
            $blogList.html("");
            loadMore();
            $loadMoreBtn.show();
            return;
        }

        $.ajax({
            url: '/blogs/search',
            method: 'GET',
            data: { keyword: keyword, page: 0, size: limit },
            success: function (data) {
                $blogList.html(data);
                offset = limit;
                $loadMoreBtn.hide();
            },
            error: function () {
                alert('Đã xảy ra lỗi khi tìm kiếm.');
            }
        });
    });

    $loadMoreBtn.on('click', function () {
        loadMore();
    });

    function loadMore() {
        $loadMoreBtn.prop('disabled', true).text('Đang tải...');

        $.ajax({
            url: '/blogs/load',
            method: 'GET',
            data: {
                offset: offset,
                limit: limit
            },
            success: function (data) {
                if (data.trim() === "") {
                    $loadMoreBtn.hide();
                } else {
                    $blogList.append(data);
                    offset += limit;
                }
            },
            error: function () {
                alert('Đã xảy ra lỗi khi tải thêm.');
            },
            complete: function () {
                $loadMoreBtn.prop('disabled', false).text('Tải thêm');
            }
        });
    }

    loadMore();
});
