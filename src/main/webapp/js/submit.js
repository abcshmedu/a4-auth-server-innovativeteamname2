'use strict';

/**
 * Ugly java script code for simple tests of shareit's REST interface.
 *  @author Axel Böttcher <axel.boettcher@hm.edu>
 */

/**
 * This function is used for transfer of new book info.
 */
var submitNewBook = function() {
    var json = JSON.stringify({
        title: $("input[name=title]").val(),
        author: $("input[name=author]").val(),
        isbn: $("input[name=isbn]").val()
    });
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/media/books/?token=rootToken',
        type:'POST',
        contentType: 'application/json; charset=UTF-8',
        data: json
        })
    .done(() => {
        $("input[name=title]").val("");
        $("input[name=author]").val("");
        $("input[name=isbn]").val("");
        
        errorText.addClass("visible");
        errorText.text("Book was added to the System.");
        errorText.removeClass("hidden");
    })
    .fail((error) => {
        errorText.addClass("visible");
        errorText.text(error.responseJSON.detail);
        errorText.removeClass("hidden");
    });
}

/**
 * This function is used for transfer of new book info.
 */
var submitNewDisc = function() {
    var json = JSON.stringify({
        title: $("input[name=title]").val(),
        director: $("input[name=director]").val(),
        barcode: $("input[name=barcode]").val(),
        fsk: $("input[name=fsk]").val()
    });
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/media/discs/?token=rootToken',
        type:'POST',
        contentType: 'application/json; charset=UTF-8',
        data: json
        })
    .done(() => {
        $("input[name=title]").val(""),
        $("input[name=director]").val(""),
        $("input[name=barcode]").val(""),
        $("input[name=fsk]").val("")
        
        errorText.addClass("visible");
        errorText.text("Disc was added to the System.");
        errorText.removeClass("hidden");
    })
    .fail((error) => {
        errorText.addClass("visible");
        errorText.text(error.responseJSON.detail);
        errorText.removeClass("hidden");
    });
}

/**
 * Creates a list of all books using a Mustache-template.
 */
var listBooks = function() {
    $.ajax({
        url: '/shareit/media/books/?token=rootToken',
        type:'GET'
    })
    .done((data) => {
        var template = "<h2>ShareIt &mdash; List Books</h2><table class='u-full-width'><tbody><tr><th>Titel</th><th>Autor</th><th>ISBN</th></tr>{{#data}}<tr><td>{{title}}</td><td>{{author}}</td><td>{{isbn}}</td></tr>{{/data}}</tbody></table>";
        Mustache.parse(template);
        var output = Mustache.render(template, {data: data});
        $("#content").html(output);
    });// no error handling
}

/**
 * Creates a list of all books using a Mustache-template.
 */
var listDiscs = function() {
    $.ajax({
        url: '/shareit/media/discs/?token=rootToken',
        type:'GET'
    })
    .done((data) => {
        var template = "<h2>ShareIt &mdash; List Discs</h2><table class='u-full-width'><tbody><tr><th>Titel</th><th>Director</th><th>Barcode</th><th>FSK</th></tr>{{#data}}<tr><td>{{title}}</td><td>{{director}}</td><td>{{barcode}}</td><td>{{fsk}}</td></tr>{{/data}}</tbody></table>";
        Mustache.parse(template);
        var output = Mustache.render(template, {data: data});
        $("#content").html(output);
    });// no error handling
}

/**
 * Updates a Book.
 */
var updateBook = function() {
    var json = JSON.stringify({
        title: $("input[name=title]").val(),
        author: $("input[name=author]").val(),
        isbn: $("input[name=isbn]").val()
    });
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/media/books/?token=rootToken',
        type:'PUT',
        contentType: 'application/json; charset=UTF-8',
        data: json
        })
    .done(() => {
        $("input[name=title]").val("");
        $("input[name=author]").val("");
        $("input[name=isbn]").val("");
        
        errorText.addClass("visible");
        errorText.text("Book was updated in the System.");
        errorText.removeClass("hidden");
    })
    .fail((error) => {
        errorText.addClass("visible");
        errorText.text(error.responseJSON.detail);
        errorText.removeClass("hidden");
    });
}

/**
 * Updates a Disc.
 */
var updateDisc = function() {
    var json = JSON.stringify({
        title: $("input[name=title]").val(),
        director: $("input[name=director]").val(),
        barcode: $("input[name=barcode]").val(),
        fsk: $("input[name=fsk]").val()
    });
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/media/discs/?token=rootToken',
        type:'PUT',
        contentType: 'application/json; charset=UTF-8',
        data: json
        })
    .done(() => {
        $("input[name=title]").val(""),
        $("input[name=director]").val(""),
        $("input[name=barcode]").val(""),
        $("input[name=fsk]").val("")
        
        errorText.addClass("visible");
        errorText.text("Disc was updated in the System.");
        errorText.removeClass("hidden");
    })
    .fail((error) => {
        errorText.addClass("visible");
        errorText.text(error.responseJSON.detail);
        errorText.removeClass("hidden");
    });
}

/**
 * Finds a book.
 */
var findBook = function() {
    var isbn = $("input[name=isbn]").val();
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/media/books/'+isbn,
        type:'GET'
        })
    .done((data) => {
        $("input[name=isbn]").val("")
        var template = "<h2>ShareIt &mdash; List Book</h2><table class='u-full-width'><tbody><tr><th>Titel</th><th>Autor</th><th>ISBN</th></tr>{{#data}}<tr><td>{{title}}</td><td>{{author}}</td><td>{{isbn}}</td></tr>{{/data}}</tbody></table>";
        Mustache.parse(template);
        var output = Mustache.render(template, {data: data});
        $("#content").html(output);
    });// no error handling because of "data is not defined"
}

/**
 * Finds a disc.
 */
var findDisc = function() {
    var barcode = $("input[name=barcode]").val();
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/media/discs/'+barcode,
        type:'GET'
        })
    .done((data) => {
        $("input[name=director]").val("")
        var template = "<h2>ShareIt &mdash; List Disc</h2><table class='u-full-width'><tbody><tr><th>Titel</th><th>Director</th><th>Barcode</th><th>FSK</th></tr>{{#data}}<tr><td>{{title}}</td><td>{{director}}</td><td>{{barcode}}</td><td>{{fsk}}</td></tr>{{/data}}</tbody></table>";
        Mustache.parse(template);
        var output = Mustache.render(template, {data: data});
        $("#content").html(output);
    });// no error handling
}





/**
 * This function is used for creating a new user.
 */
var registerUser = function() {
    var json = JSON.stringify({
        name: $("input[name=name]").val(),
        pass: $("input[name=pass]").val()
    });
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/users/',
        type:'POST',
        contentType: 'application/json; charset=UTF-8',
        data: json
        })
    .done(() => {
        $("input[name=name]").val("");
        $("input[name=pass]").val("");
                
        errorText.addClass("visible");
        errorText.text("User was added to the System.");
        errorText.removeClass("hidden");
    })
    .fail((error) => {
        errorText.addClass("visible");
        errorText.text(error.responseJSON.detail);
        errorText.removeClass("hidden");
    });
}

/**
 * This function is used for creating a new user.
 */
var loginUser = function() {
    var json = JSON.stringify({
        name: $("input[name=name]").val(),
        pass: $("input[name=pass]").val()
    });
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/users/login',
        type:'POST',
        contentType: 'application/json; charset=UTF-8',
        data: json
        })
    .done(() => {
        $("input[name=name]").val("");
        $("input[name=pass]").val("");
                
        errorText.addClass("visible");
        errorText.text("User was logged into the System.");
        errorText.removeClass("hidden");
    })
    .fail((error) => {
        errorText.addClass("visible");
        errorText.text(error.responseJSON.detail);
        errorText.removeClass("hidden");
    });
}

/**
 * Creates a list of all Users using a Mustache-template.
 */
var listUsers = function() {
    $.ajax({
        url: '/shareit/users',
        type:'GET'
    })
    .done((data) => {
        var template = "<h2>ShareIt &mdash; List Users</h2><table class='u-full-width'><tbody><tr><th>Name</th><th>Password</th><th>Role</th></tr>{{#data}}<tr><td>{{name}}</td><td>{{pass}}</td><td>{{role}}</td></tr>{{/data}}</tbody></table>";
        Mustache.parse(template);
        var output = Mustache.render(template, {data: data});
        $("#content").html(output);
    });// no error handling
}

/**
 * Updates a User.
 */
var updateUser = function() {
    var json = JSON.stringify({
        name: $("input[name=name]").val(),
        pass: $("input[name=pass]").val()
    });
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/users/?token=rootToken',
        type:'PUT',
        contentType: 'application/json; charset=UTF-8',
        data: json
        })
    .done(() => {
        $("input[name=name]").val("");
        $("input[name=pass]").val("");
        
        errorText.addClass("visible");
        errorText.text("User was updated in the System.");
        errorText.removeClass("hidden");
    })
    .fail((error) => {
        errorText.addClass("visible");
        errorText.text(error.responseJSON.detail);
        errorText.removeClass("hidden");
    });
}

/**
 * Finds a User.
 */
var findUser = function() {
    var user = $("input[name=name]").val();
    var errorText = $("#errormessage");
    $.ajax({
        url: '/shareit/users/'+user,
        type:'GET'
        })
    .done((data) => {
        $("input[name=name]").val("")
        var template = "<h2>ShareIt &mdash; List User</h2><table class='u-full-width'><tbody><tr><th>Name</th><th>Password</th><th>Role</th></tr>{{#data}}<tr><td>{{name}}</td><td>{{pass}}</td><td>{{role}}</td></tr>{{/data}}</tbody></table>";
        Mustache.parse(template);
        var output = Mustache.render(template, {data: data});
        $("#content").html(output);
    });// no error handling
}



/**
 * Call backer for "navigational buttons" in left column. Used to set content in main part.
 */
var changeContent = function(content) {
    $.ajax({
        url: content,
        type:'GET'
    })
    .done((data) => {
        $("#content").html(data);
    });// no error handling
}