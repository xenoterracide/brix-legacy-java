use clap;
use std::env;
use std::io::{self, Write};
use std::process;

use crate::app;

pub const LANGUAGE: &str = "LANGUAGE";
pub const CONFIG_NAME: &str = "CONFIG_NAME";
pub const PROJECT: &str = "PROJECT";
pub const MODULE: &str = "MODULE";

pub fn clap_matches() -> Result<clap::ArgMatches<'static>, Box<dyn std::error::Error>> {
    let err = match app::app().get_matches_from_safe(env::args_os()) {
        Ok(matches) => return Ok(matches),
        Err(err) => err,
    };
    if err.use_stderr() {
        return Err(err.into());
    }

    let _ = write!(io::stdout(), "{}", err);
    process::exit(0);
}
