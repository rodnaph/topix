
# Topix

Topix is a simple website for doing even simpler Bayesian style text analysis on topics.

## Usage

To start Topix just use Leiningen.

```bash
lein run
```

Then navigate to http://localhost:8001

### Settings

All settings are made through environment variables, you may need to restart Topix for some
of these to take effect.  Here are the settings with their defaults.

```bash
TOPIX_PORT=8001
TOPIX_MONGO_DB=topix
```

## Training Topix

Topix comes with some test data for training the filter.  To load it into your database
just start a REPL and run the trainer.

```
$> lein repl
REPL started; server listening on localhost port XXX
topix.core=> (require '[topix.trainer :as trainer])
topix.core=> (trainer/run)
```

MongoDB will then be loaded with the training data, so re/start the server and it'll get
loaded available for use.

### Example Results

Topix is very early in development, and I'm just starting to play with tweaking the matching,
but early results still show favour over the correct categories.

![Science Topic](http://pu-gh.com/topix/science.png)

![Fashion Topic](http://pu-gh.com/topix/fashion.png)

Hopefully with more tweaking like multi-word matching, root stemming, and more training results
will improve further.

## License

Distributed under the Eclipse Public License, the same as Clojure.

