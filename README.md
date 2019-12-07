# Project Outline

## Overview

My project will be an application that lets users maintain a database of song information to facilitate sampling and mashup creation.  It will allow users to log in, add song information, and query the database for suggestions based on a variety of criteria.  There will also be a public database of song information for users to test the features of the app and use alongside their user-created database.

This project was motivated by a personal need to select song combinations based on smart criteria that goes beyond sorting independently by key and tempo.

## Features

- User login: Users will be able to log in, either by creating accounts in the system, or by using a third-party authentication provider (e.g. Google Authentication).
- Add and edit song information: Users will be able to add new songs in the system by entering in a mixture of required (title, artist, key, tempo), and optional fields (notes, genres).
- Query database for song suggetions: Users will be able to view a list of existing song information in their database, sort that information by parameters such as key and tempo, and get recommended songs to combine based on several distinct criteria.
- Create custom fields and query formulas: In addition to the default database fields and suggestion queries, users will be able to add their own fields and query formulas.
- Delete song information: Users will be able to delete song information from their database.
- Add songs from public database to user-database: Users will be able to add songs from the public database to their personal/user-created database.

## Future Features / Extras

- Use data analysis to discover salient features from an existing song database, such as the million song dataset. Use this information to improve suggestions provided to users and to create additional querying options.
- Train a machine learning model to categorize songs in meaningful ways and give users the ability to apply this model to their own libraries.

## Technologies

- Java
- Spring Boot
- MySQL
- Thymeleaf templates
- Bootstrap

## What I'll Have to Learn

For the login functionality, I'll have to learn something new in either scenario. If I choose to use native login functionality for the site, I'll need to learn how to use the login approach outlined in the [`spring-filter-based-auth` repository](https://github.com/LaunchCodeEducation/spring-filter-based-auth). If I choose to use third-party authentcation, I'll need to learn how to use an OAuth2 provider such as Google Authentication.

I'll need to learn more about querying a mySQL database according to formulas using numerical data within the database.

I'll also be using test-driven development, writing tests for my code before writing the code itself. I don't have much experience doing this in Spring, so I'll have to learn how to use its testing capabilities.

## Project Tracker

https://trello.com/b/1q0C72Eu/launchcode-project-planning
