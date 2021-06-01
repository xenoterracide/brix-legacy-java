use clap::ArgMatches;
use std::process;

mod app;
mod args;
mod config;
use config::Config;

fn main() {
    if let Err(err) = args::clap_matches().and_then(next) {
        eprintln!("{}", err);
        process::exit(2);
    }
}

fn next(matches: ArgMatches<'static>) -> Result<(), Box<dyn std::error::Error>> {
    let config = Config::new(matches);
    println!("{}", config);

    process::exit(0);
}
