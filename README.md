# Voyages and Fleet Simulation Solution Microservices

We recommend reading the content of this project documentation in [this book format]().

## Sub repositories

* The `fleet-ms` folder contains the Java App developed using microprofile and deployed on Open Liberty. It uses Kafka API to produce events. See [the readme for details](./fleet-ms/README.md) about deployment and code explanations.
* The `voyages-ms` folder contains the Node.js app for the voyages microservice also created with microprofile. It uses the Kafka API to produce events when an order has been assigned to a voyage.

### Building this booklet locally

The content of this repository is written with markdown files, packaged with [MkDocs](https://www.mkdocs.org/) and can be built into a book-readable format by MkDocs build processes.

1. Install MkDocs locally following the [official documentation instructions](https://www.mkdocs.org/#installation).
2. `git clone https://github.com/ibm-cloud-architecture/refarch-eda.git` _(or your forked repository if you plan to edit)_
3. `cd refarch-eda`
4. `mkdocs serve`
5. Go to `http://127.0.0.1:8000/` in your browser.

### Pushing the book to GitHub Pages

1. Ensure that all your local changes to the `master` branch have been committed and pushed to the remote repository.
   1. `git push origin master`
2. Ensure that you have the latest commits to the `gh-pages` branch, so you can get others' updates.
	```bash
	git checkout gh-pages
	git pull origin gh-pages
	
	git checkout master
	```
3. Run `mkdocs gh-deploy` from the root refarch-eda directory.

--- 

## Contribute

We welcome your contributions. There are multiple ways to contribute: report bugs and improvement suggestion, improve documentation and contribute code.
We really value contributions and to maximize the impact of code contributions we request that any contributions follow these guidelines:

The [contributing guidelines are in this note.](./CONTRIBUTING.md)

## Contributors
If you want to contribute please read [this note.](CONTRIBUTING.md)
* Lead development [Jerome Boyer](https://www.linkedin.com/in/jeromeboyer/)
* Developer [Hemankita Perabathini](https://www.linkedin.com/in/hemankita-perabathini/)
* Developer [Edoardo Comar](https://www.linkedin.com/in/edoardo-comar/)
* Developer [Mickael Maison](https://www.linkedin.com/in/mickaelmaison/)

Please [contact me](mailto:boyerje@us.ibm.com) for any questions.
