
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

## License

Distributed under the Eclipse Public License, the same as Clojure.

